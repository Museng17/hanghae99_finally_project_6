package com.hanghae99.finalproject.controller;

import com.hanghae99.finalproject.model.entity.Follow;
import com.hanghae99.finalproject.model.entity.Users;
import com.hanghae99.finalproject.model.repository.FollowRepository;
import com.hanghae99.finalproject.service.FollowService;
import com.hanghae99.finalproject.util.UserinfoHttpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class FollowController {

    private final FollowRepository followRepository;
    private final FollowService followService;
    private final UserinfoHttpRequest userinfoHttpRequest;

    @PostMapping("/follow/{followerId}")
    public Follow followUser(@PathVariable long followerId, HttpServletRequest request) {
        Users user = userinfoHttpRequest.userFindByToken(request);
        return followService.save(user.getId(), followerId);
    }

    @DeleteMapping("/unfollow/{followingId}")
    public void unFollowUser(@PathVariable long followingId, HttpServletRequest request) {
        Users user = userinfoHttpRequest.userFindByToken(request);
        Long id = followService.getFollowId(user.getId(), followingId);
        followRepository.deleteById(id);
    }
}
