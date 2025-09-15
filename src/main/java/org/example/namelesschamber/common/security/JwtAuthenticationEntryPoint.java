package org.example.namelesschamber.common.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.namelesschamber.common.exception.CustomAuthenticationException;
import org.example.namelesschamber.common.exception.ErrorCode;
import org.example.namelesschamber.common.response.ApiResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        ErrorCode errorCode = (authException instanceof CustomAuthenticationException e)
                ? e.getErrorCode()
                : ErrorCode.UNAUTHORIZED;

        ApiResponse<Object> body = new ApiResponse<>(false, errorCode.getCode(), errorCode.getMessage(), null);

        response.setStatus(errorCode.getStatus().value());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(body));
    }

}
