package org.example.namelesschamber.domain.feedback.dto.response;

import org.example.namelesschamber.domain.feedback.entity.Feedback;

import java.time.LocalDateTime;

public record FeedbackResponseDto(
        String feedbackId,
        String content,
        LocalDateTime createdAt
) {
    public static FeedbackResponseDto from(Feedback feedback) {
        return new FeedbackResponseDto(
                feedback.getId(),
                feedback.getContent(),
                feedback.getCreatedAt()
        );
    }
}