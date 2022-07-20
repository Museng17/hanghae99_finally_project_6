package com.hanghae99.finalproject.exceptionHandler.CustumException;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@Getter
@NoArgsConstructor
public class CustomException extends RuntimeException {

    private String massage;
    private int statusCode;

    @JsonIgnore
    private int realStatusCode;

    public CustomException(ErrorCode errorCode) {
        this.massage = errorCode.getMassage();
        this.statusCode = errorCode.getStatusCode();
        this.realStatusCode = errorCode.getRealStatusCode();
    }

    public CustomException(String message, int statusCode) {
        this.massage = message;
        this.statusCode = statusCode;
    }
}
