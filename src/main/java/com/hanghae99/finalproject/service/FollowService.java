package com.hanghae99.finalproject.service;

import com.hanghae99.finalproject.exceptionHandler.CustumException.CustomException;
import com.hanghae99.finalproject.exceptionHandler.CustumException.ErrorCode;
import com.hanghae99.finalproject.model.dto.requestDto.UserRequestDto;
import com.hanghae99.finalproject.model.dto.responseDto.FollowDto;
import com.hanghae99.finalproject.model.dto.responseDto.FollowResponseDto;
import com.hanghae99.finalproject.model.dto.responseDto.MessageResponseDto;
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
        else throw new CustomException(ErrorCode.NOT_FOUND_TARGET);
    }

    @Transactional
    public MessageResponseDto save(Long followingId, Long followerId) {
        Users following = userRepository.findFollowingById(followingId);
        Users follower = userRepository.findFollowerById(followerId);

        if (follower == null) {

            return new MessageResponseDto(501, "팔로우 할 대상이 없습니다.");
        } else if (follower == following) {

            return new MessageResponseDto(501, "팔로우 실패 : 팔로워와 팔로잉이 같습니다.");
        } else if (followRepository.findByFollowingIdAndFollowerId(followingId,followerId) != null) {

            return new MessageResponseDto(502, "이미 팔로우 하였습니다");
        }

        Follow follow = new Follow(following, follower);
        followRepository.save(follow);

        return new MessageResponseDto(200, "팔로우 성공");
    }

    @Transactional
    public FollowResponseDto findFollowingUser(int page, int size, HttpServletRequest request){
        Users following = userRepository.findFollowingById(userinfoHttpRequest.userFindByToken(request).getId());
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Follow> follow = followRepository.findAllByFollowing(pageRequest,following);
        List<Follow> follow1 = follow.getContent();
        List<UserRequestDto> follow2 = toBoardRequestDtoList(follow1);
        List<FollowDto> followDtos = toFollowRequestDtoList(follow2);
        return  new FollowResponseDto(followDtos, follow.getTotalElements());
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
            followRequestDtoList.add(new FollowDto(userRequestDto,followRepository.findFollowerCountById(userRequestDto.getId())));
        }
        return followRequestDtoList;
    }
}
