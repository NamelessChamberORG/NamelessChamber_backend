package org.example.namelesschamber.domain.readhistory.service;

import lombok.RequiredArgsConstructor;
import org.example.namelesschamber.common.exception.CustomException;
import org.example.namelesschamber.common.exception.ErrorCode;
import org.example.namelesschamber.domain.post.dto.response.PostPreviewListResponse;
import org.example.namelesschamber.domain.post.dto.response.PostPreviewResponseDto;
import org.example.namelesschamber.domain.post.repository.PostRepository;
import org.example.namelesschamber.domain.readhistory.entity.ReadHistory;
import org.example.namelesschamber.domain.readhistory.repository.ReadHistoryRepository;
import org.example.namelesschamber.domain.user.service.CoinService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReadHistoryService {

    private final ReadHistoryRepository readHistoryRepository;
    private final PostRepository postRepository;
    private final CoinService coinService;

    /**
     * 내가 열람한 일기 목록 조회
     */
    @Transactional(readOnly = true)
    public PostPreviewListResponse getMyReadPosts(String userId) {
        int coin = coinService.getCoin(userId);

        List<ReadHistory> histories = readHistoryRepository.findAllByUserIdOrderByReadAtDesc(userId);

        List<PostPreviewResponseDto> posts = histories.stream()
                .map(h -> postRepository.findById(h.getPostId())
                        .map(PostPreviewResponseDto::from)
                        .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND))
                )
                .toList();

        return PostPreviewListResponse.of(posts, coin);
    }

    /**
     * 특정 게시글을 이미 열람했는지 여부 확인
     */
    @Transactional(readOnly = true)
    public boolean isAlreadyRead(String userId, String postId) {
        return readHistoryRepository.existsByUserIdAndPostId(userId, postId);
    }

    /**
     * 열람 기록 저장
     */
    @Transactional
    public void record(String userId, String postId) {
        readHistoryRepository.save(ReadHistory.of(userId, postId));
    }
}
