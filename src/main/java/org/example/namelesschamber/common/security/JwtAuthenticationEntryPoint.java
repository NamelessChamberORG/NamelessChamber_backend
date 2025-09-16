package org.example.namelesschamber.common.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.namelesschamber.common.exception.CustomAuthenticationException;
import org.example.namelesschamber.common.exception.ErrorCode;
import org.example.namelesschamber.common.response.ApiResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        String uri = request.getRequestURI();

        if (uri.startsWith("/api/swagger-ui") ||
                uri.startsWith("/api/v3/api-docs") ||
                uri.startsWith("/api/swagger-resources")) {
            response.addHeader("WWW-Authenticate", "Basic realm=\"Swagger UI\"");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
            return;
        }

        ErrorCode errorCode = resolveErrorCode(authException);

        log.warn("Unauthorized request - URI: {}, errorCode: {}, rootMessage: {}, springMessage: {}",
                request.getRequestURI(),
                errorCode.name(),
                (authException.getCause() != null ? authException.getCause().getMessage() : "-"),
                authException.getMessage()
        );

        ApiResponse<Object> body = new ApiResponse<>(false, errorCode.getCode(), errorCode.getMessage(), null);

        response.setStatus(errorCode.getStatus().value());
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Cache-Control", "no-store");
        response.getWriter().write(objectMapper.writeValueAsString(body));
    }

    private ErrorCode resolveErrorCode(AuthenticationException ex) {
        if (ex instanceof CustomAuthenticationException e) {
            return e.getErrorCode();
        }
        if (ex.getCause() instanceof CustomAuthenticationException e) {
            return e.getErrorCode();
        }
        return ErrorCode.UNAUTHORIZED;
    }

}
