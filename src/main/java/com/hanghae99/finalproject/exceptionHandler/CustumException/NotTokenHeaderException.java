package com.hanghae99.finalproject.exceptionHandler.CustumException;

import lombok.*;

@Getter
@NoArgsConstructor
public class NotTokenHeaderException extends Exception {

    private String message;

    public NotTokenHeaderException(String s) {
        this.message = s;
    }
}
