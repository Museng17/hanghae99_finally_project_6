package com.hanghae99.finalproject.service;

import com.hanghae99.finalproject.model.entity.Follow;
import com.hanghae99.finalproject.model.entity.Users;
import com.hanghae99.finalproject.model.repository.FollowRepository;
import com.hanghae99.finalproject.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        else throw new RuntimeException("팔로우 취소 할 대상이 없습니다");
    }

    @Transactional
    public Follow save(Long followingId, Long followerId) {
        Users following = userRepository.findFollowingById(followingId);
        Users follower = userRepository.findFollowerById(followerId);

        if (followRepository.findFollowByFollowingAndFollower(following, follower) != null)
            throw new RuntimeException("이미 팔로우 하였습니다.");

        return followRepository.save(Follow.builder()
                .following(following)
                .follower(follower)
                .build());
    }
}
