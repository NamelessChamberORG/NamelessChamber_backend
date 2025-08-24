package org.example.namelesschamber.common.exception;

public class PostContentTooShortException extends CustomException {

    public PostContentTooShortException(){
        super(ErrorCode.POST_CONTENT_TOO_SHORT);

    }
}
