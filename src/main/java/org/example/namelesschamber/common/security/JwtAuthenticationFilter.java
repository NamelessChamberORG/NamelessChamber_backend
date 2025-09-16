package org.example.namelesschamber.common.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.namelesschamber.common.config.JwtSecurityProperties;
import org.example.namelesschamber.common.exception.CustomAuthenticationException;
import org.example.namelesschamber.common.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;
    private final JwtSecurityProperties jwtSecurityProperties;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String token = resolveToken(request);
        log.debug("JwtAuthenticationFilter - Incoming token: {}", token != null ? "present" : "null");

        if (token != null) {
            try {
                Claims claims = jwtTokenProvider.validateToken(token);
                String subject = claims.getSubject();

                log.debug("JwtAuthenticationFilter - Token validated for subject={}", subject);

                CustomPrincipal principal = new CustomPrincipal(subject, claims.get("role", String.class));
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(principal, null, List.of());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (CustomAuthenticationException ex) {
                log.warn("JwtAuthenticationFilter - Token validation failed: {}", ex.getErrorCode().getMessage());

                ResponseEntity<ApiResponse<Object>> entity =
                        ApiResponse.error(ex.getErrorCode().getStatus(), ex.getErrorCode().getCode(), ex.getErrorCode().getMessage());

                response.setStatus(entity.getStatusCode().value());
                response.setContentType("application/json;charset=UTF-8");
                response.setHeader("Cache-Control", "no-store");
                response.getWriter().write(objectMapper.writeValueAsString(entity.getBody()));
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return jwtSecurityProperties.getIgnorePaths().stream()
                .anyMatch(path::startsWith);
    }
}

