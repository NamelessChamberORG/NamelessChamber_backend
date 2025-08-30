package org.example.namelesschamber.domain.user.dto.response;

import org.example.namelesschamber.domain.user.entity.User;

public record LoginResponseDto(
        String id,
        String nickname,
        int coin,
        String accessToken,
        String refreshToken
) {
    public static LoginResponseDto of(User user, String accessToken, String refreshToken) {
        return new LoginResponseDto(
                user.getId(),
                user.getNickname(),
                user.getCoin(),
                accessToken,
                refreshToken
        );
    }
}
