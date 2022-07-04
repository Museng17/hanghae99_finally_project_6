package com.hanghae99.finalproject.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hanghae99.finalproject.model.dto.requestDto.SocialLoginRequestDto;
import com.hanghae99.finalproject.model.dto.requestDto.UserRequestDto;
import com.hanghae99.finalproject.util.TimeStamp;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Users extends TimeStamp {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = true)
    private String imgPath;

    @Column(nullable = true)
    private String information;

    @JsonIgnore
    @Column(nullable = true)
    private String password;

    public Users(String username, String nickname, String password) {
        this.username = username;
        this.nickname = nickname;
        this.password = password;
    }

    public Users(SocialLoginRequestDto socialLoginRequestDto, int allCount) {
        this.username = socialLoginRequestDto.getEmail();
        this.nickname = "USER(" + UUID.randomUUID().toString().replaceAll("-", "").substring(5, 9) + allCount + ")";
    }

    public void update(UserRequestDto userRequestDto) {
        this.nickname = userRequestDto.getNickname();
        this.imgPath = userRequestDto.getImgPath();
        this.information = userRequestDto.getInformation();
    }

    public void updatePw(String newPassword) {

        this.password = newPassword;
    }
}
