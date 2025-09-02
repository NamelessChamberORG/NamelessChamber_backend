package org.example.namelesschamber.domain.feedback.dto.request;

import jakarta.validation.constraints.NotBlank;

public record FeedbackRequestDto(
        @NotBlank
        String content) {
}