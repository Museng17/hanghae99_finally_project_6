package com.hanghae99.finalproject.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hanghae99.finalproject.model.dto.requestDto.*;
import com.hanghae99.finalproject.model.resultType.*;
import lombok.*;

import javax.persistence.*;

import java.util.*;

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

    @Column(nullable = true)
    private String email;

    @Column(nullable = false)
    private Long folderCnt;

    @Column(nullable = false)
    private Long boardCnt;

    @Column(nullable = false)
    private Long reportCnt = 0L;

    @Column(nullable = false)
    private LoginType loginType;

    @OneToMany
    @JoinColumn(name = "folder_id")
    private List<Folder> folderList;

    @OneToMany
    @JoinColumn(name = "board_id")
    private List<Board> boardList;

    public Users(UserRequestDto Dto) {
        this.username = Dto.getUsername();
        this.nickname = Dto.getNickname();
        this.password = Dto.getPassword();
        this.loginType = LoginType.USER;
        this.boardCnt = 0L;
        this.folderCnt = 0L;
        this.email = Dto.getEmail();
    }

    public Users(SocialLoginRequestDto socialLoginRequestDto, int allCount) {
        this.username = socialLoginRequestDto.getEmail();
        this.nickname = "USER(" + UUID.randomUUID().toString().replaceAll("-", "").substring(5, 9) + allCount + ")";
        this.folderCnt = 0L;
        this.boardCnt = 0L;
        this.loginType = LoginType.GOOGLE;
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
