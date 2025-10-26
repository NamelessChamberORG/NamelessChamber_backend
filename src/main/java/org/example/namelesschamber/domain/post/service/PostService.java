package org.example.namelesschamber.domain.post.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.namelesschamber.common.exception.CustomException;
import org.example.namelesschamber.common.exception.ErrorCode;
import org.example.namelesschamber.domain.post.dto.request.PostCreateRequestDto;
import org.example.namelesschamber.domain.post.dto.response.PostCreateResponseDto;
import org.example.namelesschamber.domain.post.dto.response.PostDetailResponseDto;
import org.example.namelesschamber.domain.post.dto.response.PostPreviewListResponse;
import org.example.namelesschamber.domain.post.dto.response.PostPreviewResponseDto;
import org.example.namelesschamber.domain.post.entity.Post;
import org.example.namelesschamber.domain.post.entity.PostType;
import org.example.namelesschamber.domain.post.repository.PostRepository;
import org.example.namelesschamber.domain.readhistory.service.ReadHistoryService;
import org.example.namelesschamber.domain.user.service.CoinService;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.bson.Document;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final ReadHistoryService readHistoryService;
    private final CoinService coinService;
    private final CalendarService calendarService;
    private final MongoTemplate mongoTemplate;

    @PostConstruct
    void ensureIndexes() {
        // 유니크 인덱스: (user_id, day_start) 하루 한 번만 통과
        mongoTemplate.indexOps("first_write_gate")
                .ensureIndex(new Index()
                        .on("user_id", Sort.Direction.ASC)
                        .on("day_start", Sort.Direction.ASC)
                        .unique()
                        .named("uniq_user_day"));

        //TTL 인덱스(2일 후 자동 삭제)
        mongoTemplate.indexOps("first_write_gate")
                .ensureIndex(new Index()
                        .on("created_at", Sort.Direction.ASC)
                        .expire(172800)
                        .named("ttl_cleanup_2d"));
    }



    @Transactional(readOnly = true)
    public PostPreviewListResponse getPostPreviews(String userId) {
        int coin = coinService.getCoin(userId);
        List<PostPreviewResponseDto> posts = postRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(PostPreviewResponseDto::from)
                .toList();
        return PostPreviewListResponse.of(posts, coin);
    }

    @Transactional(readOnly = true)
    public PostPreviewListResponse getPostPreviews(PostType type, String userId) {
        int coin = coinService.getCoin(userId);
        List<PostPreviewResponseDto> posts = postRepository.findAllByTypeOrderByCreatedAtDesc(type).stream()
                .map(PostPreviewResponseDto::from)
                .toList();
        return PostPreviewListResponse.of(posts, coin);
    }

    public PostCreateResponseDto createPost(PostCreateRequestDto request, String userId) {
        request.type().validateContentLength(request.content());

        Post post = Post.builder()
                .title(request.title())
                .content(request.content())
                .type(request.type())
                .userId(userId)
                .build();

        postRepository.save(post);

        try {
            int coin = coinService.rewardForPost(userId, 1);
            // 첫 글 여부 판독
            boolean isFirstToday = markFirstWriteIfAbsent(userId);

            int totalPosts = Math.toIntExact(postRepository.countByUserId(userId));

            PostCreateResponseDto.WeeklyCalendarDto calendar = null;
            if (isFirstToday) {
                calendar = calendarService.computeThisWeek(userId);
            }

            return new PostCreateResponseDto(
                    post.getId(),
                    totalPosts,
                    coin,
                    isFirstToday,
                    calendar
            );

        } catch (RuntimeException e) {
            //실패 시 보상 처리
            try {
                postRepository.deleteById(post.getId()); }
            catch (RuntimeException ignore) {
                log.error("Compensation(delete post) failed. postId={}, userId={}", post.getId(), userId, ignore);
            }
            throw e;
        }
    }

    /** 오늘 첫 글 게이트 유니크 업서트. 최초만 true */
    private boolean markFirstWriteIfAbsent(String userId) {
        ZoneId KST = ZoneId.of("Asia/Seoul");
        Instant dayStartUtc = LocalDate.now(KST).atStartOfDay(KST).toInstant();

        Query q = Query.query(Criteria.where("user_id").is(userId)
                .and("day_start").is(Date.from(dayStartUtc)));
        Update u = new Update()
                .setOnInsert("user_id", userId)
                .setOnInsert("day_start", Date.from(dayStartUtc))
                .setOnInsert("created_at", new Date());

        FindAndModifyOptions opt = FindAndModifyOptions.options().upsert(true).returnNew(false);
        Document before = mongoTemplate.findAndModify(q, u, opt, Document.class, "first_write_gate");
        return (before == null); // 문서가 새로 생성된 경우만 true
    }

    public PostDetailResponseDto getPostById(String postId, String userId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        boolean charged = coinService.chargeIfEnough(userId, 1);

        if (!charged){
            log.warn("User {} does not have enough coins to read post {}", userId, postId);
            throw new CustomException(ErrorCode.NOT_ENOUGH_COIN);
        }

        boolean firstRead;
        try {
             firstRead = readHistoryService.record(userId, postId);
        } catch (RuntimeException ex) {
            coinService.refund(userId, 1);
            throw ex;
        }

        if (firstRead) {
            incrementViews(postId);
        } else {
            coinService.refund(userId, 1);
        }

        int finalCoin = coinService.getCoin(userId);

        return PostDetailResponseDto.from(post, finalCoin);

    }

    @Transactional(readOnly = true)
    public List<PostPreviewResponseDto> getMyPosts(String userId) {
        return postRepository.findAllByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(PostPreviewResponseDto::from)
                .toList();
    }

    private void incrementViews(String postId) {
        mongoTemplate.updateFirst(
                Query.query(Criteria.where("_id").is(postId)),
                new Update().inc("views", 1),
                Post.class
        );
    }

}
