package org.example.namelesschamber.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "잘못된 입력 값입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."),
    POST_CONTENT_TOO_SHORT(HttpStatus.BAD_REQUEST,"조금 더 이야기해주세요. 100자 이상 적어야 흘려보낼 수 있어요."),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 ID의 게시글을 찾을 수 없습니다."),
    POST_ALREADY_DELETED(HttpStatus.BAD_REQUEST,  "이미 삭제된 게시글입니다.");

    private final HttpStatus status;
    private final String message;
}
