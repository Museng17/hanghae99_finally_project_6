package com.hanghae99.finalproject.model.dto.responseDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class FollowResponseDto {
    private List<FollowDto> followDtos;
    private Long followingCnt;

    public FollowResponseDto(List<FollowDto> followDtos, Long followingCnt){
        this.followDtos = followDtos;
        this.followingCnt = followingCnt;
    }
}
