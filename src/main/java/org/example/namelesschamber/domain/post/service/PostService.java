package org.example.namelesschamber.domain.post.service;

import lombok.RequiredArgsConstructor;
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
import org.example.namelesschamber.domain.readhistory.entity.ReadHistory;
import org.example.namelesschamber.domain.readhistory.repository.ReadHistoryRepository;
import org.example.namelesschamber.domain.readhistory.service.ReadHistoryService;
import org.example.namelesschamber.domain.user.service.CoinService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final ReadHistoryService readHistoryService;
    private final CoinService coinService;

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

    @Transactional
    public PostCreateResponseDto createPost(PostCreateRequestDto request, String userId) {
        request.type().validateContentLength(request.content());

        Post post = Post.builder()
                .title(request.title())
                .content(request.content())
                .type(request.type())
                .userId(userId)
                .build();

        postRepository.save(post);

        int coinAfterCreate = coinService.rewardForPost(userId, 1);

        return new PostCreateResponseDto(coinAfterCreate, post.getId());
    }

    @Transactional
    public PostDetailResponseDto getPostById(String postId, String userId) {

        boolean alreadyRead = readHistoryService.isAlreadyRead(userId, postId);

        int coinAfterCharge = alreadyRead
                ? coinService.getCoin(userId)
                : coinService.chargeForRead(userId, 1);

        if (!alreadyRead) {
            readHistoryService.record(userId, postId);
        }

        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        post.increaseViews();
        postRepository.save(post);

        return PostDetailResponseDto.from(post, coinAfterCharge);
    }
}
