package org.example.namelesschamber.domain.feedback.dto.response;

import org.example.namelesschamber.domain.feedback.entity.Feedback;

import java.time.LocalDateTime;

public record FeedbackResponseDto(
        String id,
        String userId,
        String content,
        LocalDateTime createdAt
) {
    public static FeedbackResponseDto from(Feedback feedback) {
        return new FeedbackResponseDto(
                feedback.getId(),
                feedback.getUserId(),
                feedback.getContent(),
                feedback.getCreatedAt()
        );
    }
}