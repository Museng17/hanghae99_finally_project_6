package com.hanghae99.finalproject.model.repository;

import com.hanghae99.finalproject.model.entity.Users;
import org.springframework.data.jpa.repository.*;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByUsername(String username);

    Optional<Users> findByEmail(String email);

    Optional<Users> findById(Long id);

    Optional<Users> findByNickname(String nickname);

    @Query("select count(u.id) from Users u")
    int findAllCount();

    Users findFollowerById(Long id);

    Users findFollowingById(Long id);

    @Query("select new Users(u.id, u.imgPath, u.information, u.nickname, u.username) from Users u where u.username = ?1")
    Optional<Users> findByUsernameNoJoin(String toString);
}
