package org.example.namelesschamber.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.example.namelesschamber.common.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.context.support.DefaultMessageSourceResolvable;

import java.util.Objects;
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<Object>> handleCustomException(CustomException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        log.warn("CustomException: {} - {}", errorCode.getCode(), errorCode.getMessage());
        return ApiResponse.error(
                errorCode.getStatus(),
                errorCode.getCode(),
                errorCode.getMessage()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleException(Exception ex) {
        log.error("Unhandled exception", ex);
        return ApiResponse.error(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ErrorCode.INTERNAL_SERVER_ERROR.getCode(),
                ErrorCode.INTERNAL_SERVER_ERROR.getMessage()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(ErrorCode.INVALID_INPUT.getMessage());

        log.warn("Validation failed: {}", errorMessage);

        return ApiResponse.error(
                ErrorCode.INVALID_INPUT.getStatus(),
                ErrorCode.INVALID_INPUT.getCode(),
                errorMessage
        );
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Object>> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        log.warn("Unsupported HTTP method: {}", ex.getMethod());
        return ApiResponse.error(
                ErrorCode.METHOD_NOT_ALLOWED.getStatus(),
                ErrorCode.METHOD_NOT_ALLOWED.getCode(),
                ErrorCode.METHOD_NOT_ALLOWED.getMessage()
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Object>> handleMessageNotReadable(HttpMessageNotReadableException ex) {
        log.warn("Malformed JSON request: {}", ex.getMessage());
        return ApiResponse.error(
                ErrorCode.INVALID_JSON.getStatus(),
                ErrorCode.INVALID_JSON.getCode(),
                ErrorCode.INVALID_JSON.getMessage()
        );
    }

}
