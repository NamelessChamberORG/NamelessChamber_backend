package org.example.namelesschamber.admin.user.service;

import lombok.RequiredArgsConstructor;
import org.example.namelesschamber.admin.user.dto.request.AdminUserRequestDto;
import org.example.namelesschamber.admin.user.dto.response.AdminUserResponseDto;
import org.example.namelesschamber.common.exception.CustomException;
import org.example.namelesschamber.common.exception.ErrorCode;
import org.example.namelesschamber.domain.user.entity.User;
import org.example.namelesschamber.domain.user.entity.UserRole;
import org.example.namelesschamber.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<AdminUserResponseDto> getAllUsers() {
        return userRepository.findAllByUserRole(UserRole.USER).stream()
                .map(AdminUserResponseDto::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public AdminUserResponseDto getUserById(String id) {
        return userRepository.findByIdAndUserRole(id, UserRole.USER)
                .map(AdminUserResponseDto::from)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public AdminUserResponseDto getUserByNickname(String nickname) {
        return userRepository.findByNicknameAndUserRole(nickname, UserRole.USER)
                .map(AdminUserResponseDto::from)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }


    @Transactional
    public void updateUserStatus(String id, AdminUserRequestDto request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        user.changeStatus(request.status());
        userRepository.save(user);
    }
}