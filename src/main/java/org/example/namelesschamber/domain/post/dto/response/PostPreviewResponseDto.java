package org.example.namelesschamber.domain.post.dto.response;

import org.example.namelesschamber.domain.post.entity.Post;

import java.time.LocalDateTime;

public record PostPreviewResponseDto(
        String id,
        String userId,
        String title,
        String contentPreview,
        int contentLength,
        long likes,
        long views,
        LocalDateTime createdAt
) {
    private static final int PREVIEW_MAX_LENGTH = 100;
    private static final String ELLIPSIS = "...";

    public static PostPreviewResponseDto from(Post post) {
        String content = post.getContent() == null ? "" : post.getContent();

        String preview = content.length() > PREVIEW_MAX_LENGTH
                ? content.substring(0, PREVIEW_MAX_LENGTH) + ELLIPSIS
                : content;

        return new PostPreviewResponseDto(
                post.getId(),
                post.getUserId(),
                post.getTitle(),
                preview,
                content.length(),
                post.getLikes(),
                post.getViews(),
                post.getCreatedAt()
        );
    }
}
