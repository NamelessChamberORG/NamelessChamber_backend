package org.example.namelesschamber.common.exception;

public class PostNotFoundException extends CustomException {
    public PostNotFoundException(String id) {
        super(ErrorCode.POST_NOT_FOUND);
    }
}
