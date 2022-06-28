package com.hanghae99.finalproject.model.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class SignInDto {
    private String username;
    private String password;
}