package org.example.namelesschamber.admin.user.dto.response;

import org.example.namelesschamber.domain.user.entity.User;
import org.example.namelesschamber.domain.user.entity.UserRole;
import org.example.namelesschamber.domain.user.entity.UserStatus;

import java.time.LocalDateTime;

public record AdminUserResponseDto(
        String id,
        String nickname,
        String email,
        UserRole userRole,
        UserStatus status,
        LocalDateTime createdAt
) {
    public static AdminUserResponseDto from(User user) {
        return new AdminUserResponseDto(
                user.getId(),
                user.getNickname(),
                user.getEmail(),
                user.getUserRole(),
                user.getStatus(),
                user.getCreatedAt()
        );
    }
}