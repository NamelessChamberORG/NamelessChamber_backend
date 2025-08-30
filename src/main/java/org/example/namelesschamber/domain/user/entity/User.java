package org.example.namelesschamber.domain.user.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User {

    @Id
    private String id;

    @Indexed(unique = true, sparse = true)
    private String nickname;

    private String passwordHash;

    @Builder.Default
    private int coin = 0;

    @Builder.Default
    private UserStatus status = UserStatus.ACTIVE;

    @CreatedDate
    private LocalDateTime createdAt;

    private LocalDateTime lastLoginAt;

    private boolean anonymous;

    @Indexed(unique = true, sparse = true)
    private String anonymousToken;

}
