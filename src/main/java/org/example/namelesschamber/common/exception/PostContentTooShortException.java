package org.example.namelesschamber.common.exception;

public class PostContentTooShortException extends CustomException {

    public PostContentTooShortException(){
        super(ErrorCode.POST_NOT_TOO_SHORT);

    }
}
