package com.hanghae99.finalproject.exceptionHandler.CustumException;

import lombok.*;

@Getter
@NoArgsConstructor
public class CustomException extends RuntimeException {

    private String massage;
    private int statusCode;

    public CustomException(ErrorCode errorCode) {
        this.massage = errorCode.getMassage();
        this.statusCode = errorCode.getStatusCode();
    }

    public CustomException(String message, int statusCode) {
        this.massage = message;
        this.statusCode = statusCode;
    }
}
