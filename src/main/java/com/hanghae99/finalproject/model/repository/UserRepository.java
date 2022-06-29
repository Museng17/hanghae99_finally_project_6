package com.hanghae99.finalproject.model.repository;

import com.hanghae99.finalproject.model.entity.Users;
import org.springframework.data.jpa.repository.*;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByUsername(String username);

    Optional<Users> findById(Long id);

    Optional<Users> findByNickname(String nickname);

    @Query("select count(u.id) from Users u")
    int findAllCount();
}
