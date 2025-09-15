package org.example.namelesschamber.domain.user.service;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.example.namelesschamber.common.exception.CustomException;
import org.example.namelesschamber.common.exception.ErrorCode;
import org.example.namelesschamber.common.security.JwtTokenProvider;
import org.example.namelesschamber.common.util.EncoderUtils;
import org.example.namelesschamber.domain.user.dto.request.LoginRequestDto;
import org.example.namelesschamber.domain.user.dto.request.SignupRequestDto;
import org.example.namelesschamber.domain.user.dto.response.LoginResponseDto;
import org.example.namelesschamber.domain.user.dto.response.UserInfoResponseDto;
import org.example.namelesschamber.domain.user.entity.RefreshToken;
import org.example.namelesschamber.domain.user.entity.User;
import org.example.namelesschamber.domain.user.entity.UserRole;
import org.example.namelesschamber.domain.user.entity.UserStatus;
import org.example.namelesschamber.domain.user.repository.RefreshTokenRepository;
import org.example.namelesschamber.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final EncoderUtils encoderUtils;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${refresh.expiration}")
    private long refreshValidityInMs;


    @Transactional
    public LoginResponseDto signup(SignupRequestDto request, String subject) {

        // 1) 익명 → 회원 전환
        if (subject != null) {
            User currentUser = userRepository.findById(subject)
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

            if (currentUser.getUserRole() == UserRole.USER) {
                throw new CustomException(ErrorCode.ALREADY_REGISTERED);
            }

            if (userRepository.existsByEmail(request.email())) {
                throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
            }

            currentUser.updateToMember(
                    request.email(),
                    encoderUtils.encode(request.password())
            );

            userRepository.save(currentUser);

            return issueTokens(currentUser);
        }

        // 2) 신규 회원가입
        if (userRepository.existsByEmail(request.email())) {
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }

        User user = User.builder()
                .email(request.email())
                .passwordHash(encoderUtils.encode(request.password()))
                .userRole(UserRole.USER)
                .build();

        userRepository.save(user);

        return issueTokens(user);
    }


    @Transactional
    public LoginResponseDto login(LoginRequestDto request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!encoderUtils.matches(request.password(), user.getPasswordHash())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new CustomException(ErrorCode.USER_NOT_ACTIVE);
        }

        return issueTokens(user);
    }


    @Transactional
    public LoginResponseDto loginAsAnonymous() {

        User user = User.builder()
                .userRole(UserRole.ANONYMOUS)
                .expiresAt(LocalDateTime.now().plusDays(7)) // 7일 TTL
                .build();

        userRepository.save(user);

        return issueTokens(user);
    }


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

    @Transactional
    public LoginResponseDto issueTokens(User user) {
        String accessToken = jwtTokenProvider.createAccessToken(user.getId(), user.getUserRole().name());

        String rawRefreshToken = UUID.randomUUID().toString();
        String encodedRefreshToken = encoderUtils.encode(rawRefreshToken);

        LocalDateTime expiryDate = LocalDateTime.now()
                .plus(Duration.ofMillis(refreshValidityInMs));

        RefreshToken newToken = RefreshToken.builder()
                .userId(user.getId())
                .token(encodedRefreshToken)
                .expiryDate(expiryDate)
                .build();

        refreshTokenRepository.deleteByUserId(user.getId());
        refreshTokenRepository.save(newToken);

        return LoginResponseDto.of(user, accessToken, rawRefreshToken);
    }

    @Transactional
    public LoginResponseDto reissueTokens(String accessToken, String refreshToken) {
        Claims claims = jwtTokenProvider.getClaimsEvenIfExpired(accessToken);
        String userId = claims.getSubject();

        RefreshToken saved = refreshTokenRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_TOKEN));

        if (!encoderUtils.matches(refreshToken, saved.getToken())) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
        if (saved.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new CustomException(ErrorCode.TOKEN_EXPIRED);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return issueTokens(user);
    }

    @Transactional
    public void logout(String userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }
}

