package org.example.namelesschamber.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;

public record SignupRequestDto(
        @NotBlank
        String nickname,
        @NotBlank
        String password
) {

}
