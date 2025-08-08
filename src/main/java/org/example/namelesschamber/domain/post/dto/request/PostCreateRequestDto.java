package org.example.namelesschamber.domain.post.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.example.namelesschamber.domain.post.entity.PostType;

public record PostCreateRequestDto(
        @NotBlank
        String content,
        @NotNull
        PostType type
) {
}
