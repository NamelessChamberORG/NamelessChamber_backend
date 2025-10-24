package org.example.namelesschamber.domain.post.service;

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
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final ReadHistoryService readHistoryService;
    private final CoinService coinService;
    private final MongoTemplate mongoTemplate;


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
            return new PostCreateResponseDto(coin, post.getId());
        } catch (RuntimeException e) {
            try {
                postRepository.deleteById(post.getId());
            } catch (RuntimeException ignore) {
                log.error("Compensation(delete post) failed. postId={}, userId={}", post.getId(), userId, ignore);
            }
            throw e;
        }
    }

    public PostDetailResponseDto getPostById(String postId, String userId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        boolean charged = coinService.chargeIfEnough(userId, 1);
        if (!charged) throw new CustomException(ErrorCode.NOT_ENOUGH_COIN);


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
