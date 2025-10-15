package org.example.namelesschamber.domain.visithistory.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document("visit_history")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VisitHistory {
    @Id
    private String id;        // userId#yyyy-MM-dd  (kst)
    private String userId;
    private String date;      // yyyy-MM-dd
    private LocalDateTime createdAt;
}