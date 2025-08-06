package org.example.namelesschamber.domain.post.dto.response;

import org.example.namelesschamber.domain.post.entity.Post;

import java.time.LocalDateTime;

public record PostResponseDto(
        String content,
        String type,
        LocalDateTime createdAt
) {

    public static PostResponseDto from(Post post){
        return new PostResponseDto(
                post.getContent(),
                post.getType().name().toLowerCase(),
                post.getCreatedAt()
        );
    }
}
