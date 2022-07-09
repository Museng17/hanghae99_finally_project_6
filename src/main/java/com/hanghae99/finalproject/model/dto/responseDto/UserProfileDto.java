package com.hanghae99.finalproject.model.dto.responseDto;

import lombok.*;

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
}
