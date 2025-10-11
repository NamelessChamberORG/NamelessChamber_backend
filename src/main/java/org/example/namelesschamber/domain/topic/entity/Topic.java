package org.example.namelesschamber.domain.topic.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document("topic")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Topic {
    @Id
    private String id;
    private String title;
    private String subtitle;
    private TopicStatus status;
    private LocalDate publishedDate;

    public void publish(LocalDate date) {
        this.status = TopicStatus.PUBLISHED;
        this.publishedDate = date;
    }

    public void reset() {
        this.status = TopicStatus.READY;
        this.publishedDate = null;
    }

    public void setStatus(TopicStatus status) {
        this.status = status;
    }
}