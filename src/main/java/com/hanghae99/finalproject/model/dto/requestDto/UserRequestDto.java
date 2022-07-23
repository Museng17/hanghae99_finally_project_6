package com.hanghae99.finalproject.model.dto.requestDto;

import com.hanghae99.finalproject.model.entity.Follow;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class UserRequestDto {
    private Long id;
    private String username;
    private String nickname;
    private String imgPath;
    private String information;
    private String password;
    private String newPassword;
    private String email;
    private Long boardCnt;
    private Long folderCnt;

    public UserRequestDto(Follow follower) {
        this.id = follower.getFollower().getId();
        this.nickname = follower.getFollower().getNickname();
        this.imgPath = follower.getFollower().getImgPath();
        this.information = follower.getFollower().getInformation();
        this.boardCnt = follower.getFollower().getBoardCnt();
    }
}
