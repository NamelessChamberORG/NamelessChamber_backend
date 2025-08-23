package org.example.namelesschamber.domain.post.dto.response;

import org.example.namelesschamber.domain.post.entity.Post;

import java.time.LocalDateTime;

public record PostDetailResponseDto(
        String id,
        String title,
        String content,
        long likes,
        long views,
        LocalDateTime createdAt
) {
    public static PostDetailResponseDto from(Post post) {
        return new PostDetailResponseDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getLikes(),
                post.getViews(),
                post.getCreatedAt()
        );
    }
}
