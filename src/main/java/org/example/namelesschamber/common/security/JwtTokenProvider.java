package org.example.namelesschamber.common.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final Key key;
    private final long validityInMs;
    private final JwtParser parser;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long validityInMs) {

        //토큰 키 길이 검증
        byte[] secretBytes = secret.getBytes(StandardCharsets.UTF_8);
        if (secretBytes.length < 32) {
            throw new IllegalArgumentException("JWT secret key is too short");
        }

        this.key = Keys.hmacShaKeyFor(secretBytes);
        this.validityInMs = validityInMs;

        // 시계 오차 허용(운영 권장): 60초
        this.parser = Jwts.parserBuilder()
                .setSigningKey(key)
                .setAllowedClockSkewSeconds(60)
                .build();
    }

    /**
     * 토큰 생성
     * @param subject 회원이면 userId, 비회원이면 uuid
     * @param role USER / ANONYMOUS
     */
    public String createToken(String subject, String role) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + validityInMs);

        return Jwts.builder()
                .setSubject(subject)
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key)
                .compact();
    }

    /** 공통 파싱: 중복 제거용 */
    public Claims parseClaims(String token) {
        return parser.parseClaimsJws(token).getBody();
    }

    /** 유효성 검사: 파싱 성공 여부로 판단 */
    public boolean isValid(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

}
