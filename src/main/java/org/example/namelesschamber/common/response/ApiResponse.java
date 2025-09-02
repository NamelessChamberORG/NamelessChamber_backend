package org.example.namelesschamber.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ApiResponse<T> {
    private boolean success;
    private Integer errorCode;
    private String errorMsg;
    private T data;

    // 성공 (데이터 포함)
    public static <T> ResponseEntity<ApiResponse<T>> success(HttpStatus status, T data) {
        ApiResponse<T> body = new ApiResponse<>(true, null, null, data);
        return ResponseEntity.status(status).body(body);
    }

    // 성공 (데이터 없음)
    public static <T> ResponseEntity<ApiResponse<T>> success(HttpStatus status) {
        ApiResponse<T> body = new ApiResponse<>(true, null, null, null);
        return ResponseEntity.status(status).body(body);
    }

    // 에러
    public static <T> ResponseEntity<ApiResponse<T>> error(HttpStatus status, Integer errorCode, String errorMsg) {
        ApiResponse<T> body = new ApiResponse<>(false, errorCode, errorMsg, null);
        return ResponseEntity.status(status).body(body);
    }
}
