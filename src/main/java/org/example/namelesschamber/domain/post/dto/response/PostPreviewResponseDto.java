package org.example.namelesschamber.domain.post.dto.response;

import org.example.namelesschamber.domain.post.entity.Post;

import java.time.LocalDateTime;

public record PostPreviewResponseDto(
        String id,
        String title,
        String contentPreview,
        int contentLength,
        long likes,
        long views,
        LocalDateTime createdAt
) {
    public static PostPreviewResponseDto from(Post post) {
        String content = post.getContent() == null ? "" : post.getContent();

        String preview = content.length() > 100
                ? content.substring(0, 100) + "..."
                : content;

        return new PostPreviewResponseDto(
                post.getId(),
                post.getTitle(),
                preview,
                content.length(),
                post.getLikes(),
                post.getViews(),
                post.getCreatedAt()
        );
    }
}
