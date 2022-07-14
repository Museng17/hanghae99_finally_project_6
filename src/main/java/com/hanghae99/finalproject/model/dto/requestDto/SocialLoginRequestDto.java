package com.hanghae99.finalproject.model.dto.requestDto;

import lombok.*;

@Getter
@Setter
public class SocialLoginRequestDto {
    private String email;
    private String name;
    private String picture;
    private String access_token;
}
