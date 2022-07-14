package com.hanghae99.finalproject.model.dto.responseDto;

import com.hanghae99.finalproject.model.dto.requestDto.UserRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FollowDto {


    private  Long followerCnt;
    private  String information;
    private  String imgPath;
    private  String nickname;
    private  Long id;

    public FollowDto(UserRequestDto userRequestDto, Long followerCnt ) {
        this.id = userRequestDto.getId();
        this.nickname = userRequestDto.getNickname();
        this.imgPath = userRequestDto.getImgPath();
        this.information = userRequestDto.getInformation();
        this.followerCnt = followerCnt;
    }
}
