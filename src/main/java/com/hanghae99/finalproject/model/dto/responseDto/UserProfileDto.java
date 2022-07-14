package com.hanghae99.finalproject.model.dto.responseDto;

import com.hanghae99.finalproject.model.entity.Users;
import com.hanghae99.finalproject.util.resultType.CategoryType;
import lombok.*;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDto {
    private Long id;
    private boolean follow;
    private String nickname;
    private String imgPath;
    private String information;
    private long followingCnt;
    private long followerCnt;
    private long boardCnt;
    private long folderCnt;
    private List<Map<String, CategoryType>> categoryList;

    public UserProfileDto(Users user, long followerCount, long followingCount, boolean follow) {
        this.id = user.getId();
        this.follow = follow;
        this.nickname = user.getNickname();
        this.imgPath = user.getImgPath();
        this.information = user.getInformation();
        this.followingCnt = followingCount;
        this.followerCnt = followerCount;
        this.boardCnt = user.getBoardCnt();
        this.folderCnt = user.getFolderCnt();
    }
}
