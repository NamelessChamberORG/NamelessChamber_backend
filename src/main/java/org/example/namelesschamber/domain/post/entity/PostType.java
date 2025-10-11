package org.example.namelesschamber.domain.post.entity;

import org.example.namelesschamber.common.exception.CustomException;
import org.example.namelesschamber.common.exception.ErrorCode;

public enum PostType {
    SHORT(30, ErrorCode.CONTENT_TOO_SHORT_FOR_SHORT),
    LONG(100, ErrorCode.CONTENT_TOO_SHORT_FOR_LONG),
    TODAY(30, ErrorCode.CONTENT_TOO_SHORT_FOR_SHORT);

    private final int minLength;
    private final ErrorCode errorCodeForTooShort;

    PostType(int minLength, ErrorCode errorCode) {
        this.minLength = minLength;
        this.errorCodeForTooShort = errorCode;
    }

    public void validateContentLength(String content) {
        if (content.length() < this.minLength) {
            throw new CustomException(this.errorCodeForTooShort);
        }
    }
}