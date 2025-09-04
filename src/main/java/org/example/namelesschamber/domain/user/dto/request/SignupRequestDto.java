package org.example.namelesschamber.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;

public record SignupRequestDto(
        @NotBlank
        String email,
        @NotBlank
        String password
) {}
