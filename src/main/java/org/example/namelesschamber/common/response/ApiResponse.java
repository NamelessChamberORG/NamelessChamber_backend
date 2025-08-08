package org.example.namelesschamber.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ApiResponse<T> {
    private boolean success;
    private String errorMsg;
    private Integer errorCode;
    private T data;

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true,null,null, data);
    }

    public static <T> ApiResponse<T> success() {
        return new ApiResponse<>(true, null, null, null);
    }

    public static <T> ApiResponse<T> error(Integer errorCode, String errorMsg) {
        return new ApiResponse<>(false, errorMsg, errorCode, null);
    }
}