package com.hanghae99.finalproject.model.repository;

import com.hanghae99.finalproject.model.entity.Follow;
import com.hanghae99.finalproject.model.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FollowRepository  extends JpaRepository<Follow, Long> {

    Follow findByFollowingIdAndFollowerId(Long following_id, Long follower_id);

    @Query(value = "SELECT COUNT(*) FROM follow WHERE follower_Id = ?1", nativeQuery = true)
    long findFollowerCountById(long Id);

    @Query(value = "SELECT COUNT(*) FROM follow WHERE following_Id = ?1", nativeQuery = true)
    long findFollowingCountById(long id);

    Page<Follow> findAllByFollowing(Pageable pageable, Users following_id);

}
