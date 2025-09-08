package org.example.namelesschamber.domain.readhistory.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Document(collection = "read_history")
@CompoundIndex(name = "user_post_unique",
        def = "{'userId': 1, 'postId': 1}",
        unique = true)
public class ReadHistory {

    @Id
    private String id;

    private String userId;

    private String postId;

    private LocalDateTime readAt;

    public static ReadHistory of(String userId, String postId) {
        return ReadHistory.builder()
                .userId(userId)
                .postId(postId)
                .readAt(LocalDateTime.now())
                .build();
    }
}
