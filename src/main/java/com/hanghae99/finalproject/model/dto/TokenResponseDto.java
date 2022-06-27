package com.hanghae99.finalproject.model.dto;

import lombok.*;
import org.springframework.stereotype.Service;

@Getter
@Service
@NoArgsConstructor
public class TokenResponseDto {

    private String accessToken;
    private String refreshToken;

    public TokenResponseDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
