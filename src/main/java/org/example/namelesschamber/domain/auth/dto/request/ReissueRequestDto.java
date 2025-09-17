package org.example.namelesschamber.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ReissueRequestDto(
        @NotBlank String accessToken,
        @NotBlank String refreshToken
) {}