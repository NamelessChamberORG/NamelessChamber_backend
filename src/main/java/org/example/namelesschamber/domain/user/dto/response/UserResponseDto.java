package org.example.namelesschamber.domain.user.dto.response;

public record UserResponseDto(
        String id,
        String nickname,
        int coin,
        String status
) { }
