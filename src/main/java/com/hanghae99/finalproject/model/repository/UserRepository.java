package com.hanghae99.finalproject.model.repository;

import com.hanghae99.finalproject.model.entity.Users;
import org.springframework.data.jpa.repository.*;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByUsername(String username);

    Optional<Users> findByEmail(String email);

    Optional<Users> findById(Long id);

    Optional<Users> findByNickname(String nickname);

    Users findUserByEmail(String email);

    Users findFollowerById(Long id);

    Users findFollowingById(Long id);

    Optional<Object> findByUsernameOrNicknameOrEmail(String username, String nickname, String email);
}
