package org.example.namelesschamber.domain.post.dto.response;

import org.example.namelesschamber.domain.post.entity.Post;

import java.time.Instant;

public record PostDetailResponseDto(
        String postId,
        String title,
        String content,
        long likes,
        long views,
        Instant createdAt,
        int coin
) {
    public static PostDetailResponseDto from(Post post, int coin) {
        return new PostDetailResponseDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getLikes(),
                post.getViews(),
                post.getCreatedAt(),
                coin
        );
    }
}
