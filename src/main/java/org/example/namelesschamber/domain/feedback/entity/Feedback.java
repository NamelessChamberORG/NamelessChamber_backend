package org.example.namelesschamber.domain.feedback.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "feedbacks")
@Getter
@Builder
public class Feedback {

    @Id
    private String id;

    private String content;

    private LocalDateTime createdAt;

    public static Feedback create(String content) {
        return Feedback.builder()
                .content(content)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
