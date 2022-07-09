package com.hanghae99.finalproject.model.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
}
