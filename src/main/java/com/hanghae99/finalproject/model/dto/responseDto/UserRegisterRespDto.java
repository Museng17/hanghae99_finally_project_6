package com.hanghae99.finalproject.model.dto.responseDto;

import lombok.*;

@NoArgsConstructor
@Getter
@Setter
public class UserRegisterRespDto {
    int statusCode;
    boolean result;
    String errorMsg;

    public UserRegisterRespDto(int statusCode, boolean result, String errorMsg) {
        this.statusCode = statusCode;
        this.result = result;
        this.errorMsg = errorMsg;
    }
}
