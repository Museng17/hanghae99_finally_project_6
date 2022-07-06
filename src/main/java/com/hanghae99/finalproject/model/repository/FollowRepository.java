package com.hanghae99.finalproject.model.repository;

import com.hanghae99.finalproject.model.entity.Follow;
import com.hanghae99.finalproject.model.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository  extends JpaRepository<Follow, Integer> {

    Follow findFollowByFollowingAndFollower(Users following_id, Users follower_id);

}
