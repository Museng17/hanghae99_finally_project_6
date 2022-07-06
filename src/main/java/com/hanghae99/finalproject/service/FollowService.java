package com.hanghae99.finalproject.service;

import com.hanghae99.finalproject.model.entity.Follow;
import com.hanghae99.finalproject.model.entity.Users;
import com.hanghae99.finalproject.model.repository.FollowRepository;
import com.hanghae99.finalproject.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    @Transactional
    public long getFollowId(Long followingId, Long followerId) {
        Users following = userRepository.findFollowingById(followingId);
        Users follower = userRepository.findFollowerById(followerId);

        Follow follow = followRepository.findFollowByFollowingAndFollower(following, follower);

        if (follow != null) return follow.getId();
        else return -1;
    }

    @Transactional
    public Follow save(Long followingId, Long followerId) {
        Users following = userRepository.findFollowingById(followingId);
        Users follower = userRepository.findFollowerById(followerId);

        return followRepository.save(Follow.builder()
                .following(following)
                .follower(follower)
                .build());
    }
}
