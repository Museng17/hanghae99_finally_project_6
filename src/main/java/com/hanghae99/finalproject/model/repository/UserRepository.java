package com.hanghae99.finalproject.model.repository;

import com.hanghae99.finalproject.model.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository <Users, Long> {
}
