package org.example.namelesschamber.domain.post.dto.request;

import jakarta.validation.constraints.NotBlank;
import org.example.namelesschamber.domain.post.entity.PostType;

public record PostCreateRequestDto(
        @NotBlank
        String content,
        @NotBlank
        PostType type
) {
}
