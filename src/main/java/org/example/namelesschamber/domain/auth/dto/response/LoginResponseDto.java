package org.example.namelesschamber.domain.auth.dto.response;

import org.example.namelesschamber.domain.user.entity.User;

public record LoginResponseDto(
        String userId,
        String email,
        String nickname,
        int coin,
        String accessToken,
        String refreshToken,
        String role
) {
    public static LoginResponseDto of(User user, String accessToken, String refreshToken) {
        return new LoginResponseDto(
                user.getId(),
                user.getEmail(),
                user.getNickname(),
                user.getCoin(),
                accessToken,
                refreshToken,
                user.getUserRole().name()
        );
    }

}
