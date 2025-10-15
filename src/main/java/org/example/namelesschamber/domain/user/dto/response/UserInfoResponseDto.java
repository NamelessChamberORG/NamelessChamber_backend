package org.example.namelesschamber.domain.user.dto.response;

import lombok.Builder;
import org.example.namelesschamber.domain.user.entity.Streak;
import org.example.namelesschamber.domain.user.entity.User;

import java.time.LocalDateTime;

@Builder
public record UserInfoResponseDto(
        String userId,
        String nickname,
        String email,
        String role,
        int coin,
        LocalDateTime createdAt,
        int currentStreak
) {
    public static UserInfoResponseDto from(User user) {
        return UserInfoResponseDto.builder()
                .userId(user.getId())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .role(user.getUserRole().name())
                .coin(user.getCoin())
                .createdAt(user.getCreatedAt())
                .currentStreak(user.getStreak().getCurrent())
                .build();
    }
}
