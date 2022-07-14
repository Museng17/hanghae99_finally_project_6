package com.hanghae99.finalproject.model.dto.responseDto;

import com.hanghae99.finalproject.model.entity.Users;
import com.hanghae99.finalproject.util.resultType.CategoryType;
import lombok.*;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MyProfileDto {
    private Long id;
    private String nickname;
    private String imgPath;
    private String information;
    private long followingCnt;
    private long followerCnt;
    private long boardCnt;
    private long folderCnt;
    private List<Map<String, CategoryType>> categoryList;

    public MyProfileDto(Users user, long followerCount, long followingCount) {
        this.id = user.getId();
        this.nickname = user.getNickname();
        this.imgPath = user.getImgPath();
        this.information = user.getInformation();
        this.followingCnt = followingCount;
        this.followerCnt = followerCount;
        this.boardCnt = user.getBoardCnt();
        this.folderCnt = user.getFolderCnt();
    }
}
