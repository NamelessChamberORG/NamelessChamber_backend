package org.example.namelesschamber.domain.user.dto.request;

public record LoginRequestDto(
        String nickname,
        String password
) {}
