package com.hanghae99.finalproject.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class SignupDto {
    private String username;
    private String nickname;
    private String pw;
}