package org.example.namelesschamber.common.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.example.namelesschamber.common.exception.CustomException;
import org.example.namelesschamber.common.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final Key key;
    @Getter
    private final long accessValidityInMs;
    private final JwtParser parser;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-expiration}") long accessValidityInMs) {

        byte[] secretBytes = secret.getBytes(StandardCharsets.UTF_8);
        if (secretBytes.length < 32) {
            throw new IllegalArgumentException("JWT secret key is too short");
        }

        this.key = Keys.hmacShaKeyFor(secretBytes);
        this.accessValidityInMs = accessValidityInMs;

        this.parser = Jwts.parserBuilder()
                .setSigningKey(key)
                .setAllowedClockSkewSeconds(60)
                .build();
    }

    public String createAccessToken(String subject, String role) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + accessValidityInMs);

        return Jwts.builder()
                .setSubject(subject)
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key)
                .compact();
    }

    public Claims parseClaims(String token) {
        return parser.parseClaimsJws(token).getBody();
    }

    //만료된 토큰 추출, 만료는 허용하지만 나머지는 예외처리
    public Claims getClaimsEvenIfExpired(String token) {
        try {
            return parser.parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        } catch (JwtException | IllegalArgumentException e) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
    }

    //액세스 토큰 검증
    public Claims validateToken(String token) {
        try {
            return parseClaims(token);
        } catch (ExpiredJwtException e) {
            throw new CustomException(ErrorCode.TOKEN_EXPIRED);
        } catch (JwtException | IllegalArgumentException e) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
    }
}
