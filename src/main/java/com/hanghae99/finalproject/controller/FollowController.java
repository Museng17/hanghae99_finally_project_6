package com.hanghae99.finalproject.controller;

import com.hanghae99.finalproject.model.dto.responseDto.MessageResponseDto;
import com.hanghae99.finalproject.model.entity.Users;
import com.hanghae99.finalproject.model.repository.FollowRepository;
import com.hanghae99.finalproject.service.FollowService;
import com.hanghae99.finalproject.util.UserinfoHttpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class FollowController {

    private final FollowRepository followRepository;
    private final FollowService followService;
    private final UserinfoHttpRequest userinfoHttpRequest;

    @PostMapping("/follow/{followerId}")
    public MessageResponseDto followUser(@PathVariable long followerId, HttpServletRequest request) {
        Users user = userinfoHttpRequest.userFindByToken(request);

        return followService.save(user.getId(), followerId);
    }

    @DeleteMapping("/unfollow/{followerId}")
    public MessageResponseDto unFollowUser(@PathVariable long followerId, HttpServletRequest request) {
        Users user = userinfoHttpRequest.userFindByToken(request);
        Long id = followService.getFollowId(user.getId(), followerId);
        followRepository.deleteById(id);

        return new MessageResponseDto(200, "팔로우 취소 성공");
    }
}
