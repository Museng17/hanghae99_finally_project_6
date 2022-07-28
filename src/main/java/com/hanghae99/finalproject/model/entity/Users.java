package com.hanghae99.finalproject.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hanghae99.finalproject.model.dto.requestDto.*;
import com.hanghae99.finalproject.model.resultType.*;
import lombok.*;

import javax.persistence.*;

import java.util.*;

import static com.hanghae99.finalproject.model.resultType.ProfileType.profileTypes;

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

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private Long folderCnt;

    @Column(nullable = false)
    private Long boardCnt;

    @Column(nullable = false)
    private Long reportCnt = 0L;

    @Column(nullable = false)
    private LoginType loginType;

    @OneToOne
    private Image image;

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
        this.imgPath = Dto.getImgPath();
    }

    public Users(SocialLoginRequestDto socialLoginRequestDto,  int randomNum, Long maxId) {
        this.username = socialLoginRequestDto.getEmail();
        this.nickname = "익명의 사용자" + maxId;
        this.folderCnt = 0L;
        this.boardCnt = 0L;
        this.email = socialLoginRequestDto.getEmail();
        this.loginType = LoginType.GOOGLE;
        this.imgPath = profileTypes.get(randomNum).getUrl();
    }

    public Users(Long id, String imgPath, String information, String nickname, String username, String email) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.imgPath = imgPath;
        this.information = information;
        this.email = email;
    }

    public void update(UserRequestDto userRequestDto) {
        this.nickname = userRequestDto.getNickname().trim();
    }

    public void updateInfo(UserRequestDto userRequestDto) {
        this.information = userRequestDto.getInformation();
    }

    public void updatePw(String newPassword) {

        this.password = newPassword;
    }

    public void resetPw(String password) {

        this.password = password;
    }

    public void updateImg(String url) {
        this.imgPath = url;
    }

    public void updateBoardCnt(long boardCnt) {
        this.boardCnt = boardCnt;
    }

    public void updateNickName() {
        this.nickname = this.nickname + this.id;
    }
}
