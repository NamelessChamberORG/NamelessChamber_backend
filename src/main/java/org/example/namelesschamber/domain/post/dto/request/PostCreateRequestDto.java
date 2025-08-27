package org.example.namelesschamber.domain.post.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.example.namelesschamber.domain.post.entity.PostType;

public record PostCreateRequestDto(
        @NotBlank
        String title,
        @NotBlank
        @Size(min = 30, message = "조금 더 이야기해주세요. 30자 이상 적어야 흘려보낼 수 있어요.")
        String content,
        @NotNull
        PostType type
) {
}
