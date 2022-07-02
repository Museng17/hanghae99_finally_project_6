package com.hanghae99.finalproject.model.dto;

import com.hanghae99.finalproject.model.entity.Users;
import lombok.*;

@Getter
@Setter
public class UserRequestDto {
    private Long id;
    private String username;
    private String nickname;
    private String imgPath;
    private String password;
    private String newPassword;
}
