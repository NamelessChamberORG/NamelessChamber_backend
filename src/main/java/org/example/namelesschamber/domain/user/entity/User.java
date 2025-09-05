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
    private String email;

    @Indexed(unique = true, sparse = true)
    private String nickname;

    private String passwordHash;

    @Builder.Default
    private int coin = 0;

    // USER / ANONYMOUS 회원, 비회원 구분
    private UserRole userRole;

    // ACTIVE / WITHDRAWN / BANNED 같은 상태
    @Builder.Default
    private UserStatus status = UserStatus.ACTIVE;

    @CreatedDate
    private LocalDateTime createdAt;

    private LocalDateTime lastLoginAt;

    // 익명 로그인시 TTL 만료 정책을 위한 컬럼
    private LocalDateTime expiresAt;

    // 글 작성 시 코인 추가
    public void addCoin(int amount) {
        this.coin += amount;
    }

    public void decreaseCoin(){
        this.coin--;
    }

    // 익명 -> 회원가입 시 업데이트
    public void updateToMember(String email, String passwordHash) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.userRole = UserRole.USER;
        this.expiresAt = null; // 회원은 TTL 만료 대상에서 제외
        }

    //닉네임 적용
    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }
}
