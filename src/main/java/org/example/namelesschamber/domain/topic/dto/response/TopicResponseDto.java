package org.example.namelesschamber.domain.topic.dto.response;

import org.example.namelesschamber.domain.topic.entity.Topic;
import java.time.LocalDate;

public record TopicResponseDto(
        String title,
        String status,
        LocalDate publishedDate
) {
    public static TopicResponseDto from(Topic topic) {
        return new TopicResponseDto(
                topic.getTitle(),
                topic.getStatus().name(),
                topic.getPublishedDate()
        );
    }
}
