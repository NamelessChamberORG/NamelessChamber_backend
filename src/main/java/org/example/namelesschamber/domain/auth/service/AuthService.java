package org.example.namelesschamber.domain.auth.service;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.example.namelesschamber.common.exception.CustomException;
import org.example.namelesschamber.common.exception.ErrorCode;
import org.example.namelesschamber.common.util.EncoderUtils;
import org.example.namelesschamber.domain.auth.dto.request.LoginRequestDto;
import org.example.namelesschamber.domain.auth.dto.request.ReissueRequestDto;
import org.example.namelesschamber.domain.auth.dto.request.SignupRequestDto;
import org.example.namelesschamber.domain.auth.dto.response.LoginResponseDto;
import org.example.namelesschamber.domain.auth.jwt.JwtTokenProvider;
import org.example.namelesschamber.domain.auth.entity.RefreshToken;
import org.example.namelesschamber.domain.user.entity.User;
import org.example.namelesschamber.domain.user.entity.UserRole;
import org.example.namelesschamber.domain.user.entity.UserStatus;
import org.example.namelesschamber.domain.auth.repository.RefreshTokenRepository;
import org.example.namelesschamber.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final EncoderUtils encoderUtils;

    @Value("${refresh.expiration}")
    private long refreshValidityInMs;

    @Transactional
    public LoginResponseDto signup(SignupRequestDto request, String subject) {

        if (userRepository.existsByEmail(request.email())) {
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }

        // 익명 사용자 → 회원 전환
        if (subject != null) {
            User currentUser = userRepository.findById(subject)
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

            if (currentUser.getUserRole().isUser()) {
                throw new CustomException(ErrorCode.ALREADY_REGISTERED);
            }

            currentUser.updateToMember(
                    request.email(),
                    encoderUtils.encode(request.password())
            );

            userRepository.save(currentUser);
            return issueTokens(currentUser);
        }

        // 신규 회원가입
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
    public LoginResponseDto reissueTokens(ReissueRequestDto request) {
        Claims claims = jwtTokenProvider.getClaimsEvenIfExpired(request.accessToken());
        String userId = claims.getSubject();

        RefreshToken saved = refreshTokenRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_TOKEN));

        if (!encoderUtils.matches(request.refreshToken(), saved.getToken())) {
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

    private LoginResponseDto issueTokens(User user) {
        String accessToken = jwtTokenProvider.createAccessToken(user.getId(), user.getUserRole().name());
        String rawRefreshToken = UUID.randomUUID().toString();
        String encodedRefreshToken = encoderUtils.encode(rawRefreshToken);

        LocalDateTime expiryDate = LocalDateTime.now().plus(Duration.ofMillis(refreshValidityInMs));

        refreshTokenRepository.deleteByUserId(user.getId());
        refreshTokenRepository.save(RefreshToken.builder()
                .userId(user.getId())
                .token(encodedRefreshToken)
                .expiryDate(expiryDate)
                .build());

        return LoginResponseDto.of(user, accessToken, rawRefreshToken);
    }
}
