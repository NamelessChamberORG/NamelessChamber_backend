package org.example.namelesschamber.admin.user.dto.request;

import org.example.namelesschamber.domain.user.entity.UserStatus;

public record AdminUserRequestDto(
        UserStatus status
) {
}