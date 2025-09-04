package org.example.namelesschamber.domain.user.dto.response;

import org.example.namelesschamber.domain.user.entity.User;
import org.example.namelesschamber.domain.user.entity.UserRole;

public record LoginResponseDto(
        String id,
        String nickname,
        int coin,
        String accessToken,
        String refreshToken,
        String role // USER / ANONYMOUS
) {
    public static LoginResponseDto of(User user, String accessToken, String refreshToken) {
        return new LoginResponseDto(
                user.getId(),
                user.getNickname(),
                user.getCoin(),
                accessToken,
                refreshToken,
                UserRole.USER.name()
        );
    }

    public static LoginResponseDto anonymous(String uuid, String accessToken) {
        return new LoginResponseDto(
                uuid,
                UserRole.ANONYMOUS.name().toLowerCase(),
                0,
                accessToken,
                null,
                UserRole.ANONYMOUS.name()
        );
    }
}
