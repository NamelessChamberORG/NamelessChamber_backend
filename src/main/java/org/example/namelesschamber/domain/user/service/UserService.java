package org.example.namelesschamber.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.example.namelesschamber.common.exception.CustomException;
import org.example.namelesschamber.common.exception.ErrorCode;
import org.example.namelesschamber.common.util.EncoderUtils;
import org.example.namelesschamber.domain.user.dto.request.LoginRequestDto;
import org.example.namelesschamber.domain.user.dto.request.SignupRequestDto;
import org.example.namelesschamber.domain.user.dto.response.LoginResponseDto;
import org.example.namelesschamber.domain.user.entity.User;
import org.example.namelesschamber.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final EncoderUtils encoderUtils;

    @Transactional
    public LoginResponseDto signup(SignupRequestDto request) {
        if (userRepository.existsByNickname(request.nickname())) {
            throw new CustomException(ErrorCode.DUPLICATE_NICKNAME);
        }

        User user = User.builder()
                .nickname(request.nickname())
                .passwordHash(encoderUtils.encode(request.password()))
                .build();

        userRepository.save(user);

        return LoginResponseDto.of(user,null,null);
    }

    @Transactional(readOnly = true)
    public LoginResponseDto login(LoginRequestDto request) {
        User user = userRepository.findByNickname(request.nickname())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!encoderUtils.matches(request.password(), user.getPasswordHash())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        return LoginResponseDto.of(user, null, null);
    }

}

