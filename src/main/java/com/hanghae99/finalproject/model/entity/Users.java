package com.hanghae99.finalproject.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hanghae99.finalproject.model.dto.requestDto.SocialLoginRequestDto;
import com.hanghae99.finalproject.model.dto.requestDto.UserRequestDto;
import com.hanghae99.finalproject.model.dto.responseDto.FileUploadResponse;
import com.hanghae99.finalproject.util.TimeStamp;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
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

    @OneToMany
    @JoinColumn(name = "folder_id")
    private List<Folder> folderList;

    @OneToMany
    @JoinColumn(name = "board_id")
    private List<Board> boardList;

    public Users(String username, String nickname, String password) {
        this.username = username;
        this.nickname = nickname;
        this.password = password;
    }

    public Users(SocialLoginRequestDto socialLoginRequestDto, int allCount) {
        this.username = socialLoginRequestDto.getEmail();
        this.nickname = "USER(" + UUID.randomUUID().toString().replaceAll("-", "").substring(5, 9) + allCount + ")";
    }

    public Users(Long id, String imgPath, String information, String nickname, String username) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.imgPath = imgPath;
        this.information = information;
    }

    public void update(UserRequestDto userRequestDto) {
        this.nickname = userRequestDto.getNickname();
    }

    public void updateInfo(UserRequestDto userRequestDto) {
        this.information = userRequestDto.getInformation();
    }

    public void updatePw(String newPassword) {

        this.password = newPassword;
    }

    public void updateImg(String url) {
        this.imgPath = url;
    }
}
