package org.example.namelesschamber.domain.user.dto.response;

import org.example.namelesschamber.domain.user.entity.User;

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
                "USER"
        );
    }

    public static LoginResponseDto anonymous(String uuid, String accessToken) {
        return new LoginResponseDto(
                uuid,
                "anonymous",
                0,
                accessToken,
                null,
                "ANONYMOUS"
        );
    }
}
