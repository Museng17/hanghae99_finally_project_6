package com.hanghae99.finalproject.service;

import com.hanghae99.finalproject.model.dto.requestDto.UserRequestDto;
import com.hanghae99.finalproject.model.dto.responseDto.FollowDto;
import com.hanghae99.finalproject.model.dto.responseDto.FollowResponseDto;
import com.hanghae99.finalproject.model.entity.Follow;
import com.hanghae99.finalproject.model.entity.Users;
import com.hanghae99.finalproject.model.repository.FollowRepository;
import com.hanghae99.finalproject.model.repository.UserRepository;
import com.hanghae99.finalproject.util.UserinfoHttpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    private final UserinfoHttpRequest userinfoHttpRequest;

    @Transactional
    public long getFollowId(Long followingId, Long followerId) {

        Follow follow = followRepository.findByFollowingIdAndFollowerId(followingId, followerId);

        if (follow != null) return follow.getId();
        else throw new RuntimeException("팔로우 취소 할 대상이 없습니다");
    }

    @Transactional
    public Follow save(Long followingId, Long followerId) {
        Users following = userRepository.findFollowingById(followingId);
        Users follower = userRepository.findFollowerById(followerId);

        if (followRepository.findByFollowingIdAndFollowerId(followingId, followerId) != null)
            throw new RuntimeException("이미 팔로우 하였습니다.");

        return followRepository.save(Follow.builder()
                .following(following)
                .follower(follower)
                .build());
    }

    @Transactional
    public FollowResponseDto findFollowingUser(int page, int size, HttpServletRequest request){
        Users following = userRepository.findFollowingById(userinfoHttpRequest.userFindByToken(request).getId());
        PageRequest pageRequest = PageRequest.of(page, size);
        Long follows = followRepository.findFollowingCountById(following.getId());
        Page<Follow> follow = followRepository.findAllByFollowing(pageRequest,following);
        List<Follow> follow1 = follow.getContent();
        List<UserRequestDto> follow2 = toBoardRequestDtoList(follow1);
        List<FollowDto> followDtos = toFollowRequestDtoList(follow2);
        return  new FollowResponseDto(followDtos, follows);
    }
    private List<UserRequestDto> toBoardRequestDtoList(List<Follow> follows) {
        List<UserRequestDto> boardRequestDtoList = new ArrayList<>();

        for (Follow follow : follows) {
            boardRequestDtoList.add(new UserRequestDto(follow));
        }
        return boardRequestDtoList;
    }
    private List<FollowDto> toFollowRequestDtoList(List<UserRequestDto> followusers){
        List<FollowDto> followRequestDtoList = new ArrayList<>();

        for (UserRequestDto userRequestDto : followusers){
            Long followercnt = followRepository.findFollowerCountById(userRequestDto.getId());
            followRequestDtoList.add(new FollowDto(userRequestDto,followercnt));
        }
        return followRequestDtoList;
    }
}
