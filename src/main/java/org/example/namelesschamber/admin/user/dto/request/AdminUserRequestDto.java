package org.example.namelesschamber.admin.user.dto.request;

import jakarta.validation.constraints.NotNull;
import org.example.namelesschamber.domain.user.entity.UserStatus;

public record AdminUserRequestDto(
        @NotNull
        UserStatus status
) {
}