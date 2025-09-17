package org.example.namelesschamber.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.example.namelesschamber.common.exception.CustomException;
import org.example.namelesschamber.common.exception.ErrorCode;
import org.example.namelesschamber.domain.user.dto.response.UserInfoResponseDto;
import org.example.namelesschamber.domain.user.entity.User;
import org.example.namelesschamber.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void updateNickname(String userId, String nickname) {
        if (userRepository.existsByNickname(nickname)) {
            throw new CustomException(ErrorCode.DUPLICATE_NICKNAME);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        user.updateNickname(nickname);
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public UserInfoResponseDto getMyInfo(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return UserInfoResponseDto.from(user);
    }
}

