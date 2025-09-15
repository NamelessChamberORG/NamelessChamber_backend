package org.example.namelesschamber.domain.user.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "refresh_tokens")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class RefreshToken {

    @Id
    private String id;

    @Indexed(unique = true)
    private String userId;

    private String token;

    private LocalDateTime expiryDate;
}
