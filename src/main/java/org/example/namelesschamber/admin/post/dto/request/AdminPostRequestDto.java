package org.example.namelesschamber.admin.post.dto.request;

import jakarta.validation.constraints.NotNull;

public record AdminPostRequestDto(
        @NotNull
        String title,
        @NotNull
        String content
) {
}