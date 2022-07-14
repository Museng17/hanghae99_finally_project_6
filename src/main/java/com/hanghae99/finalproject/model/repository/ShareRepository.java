package com.hanghae99.finalproject.model.repository;

import com.hanghae99.finalproject.model.entity.Share;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface ShareRepository extends JpaRepository<Share, Long> {
    Optional<Share> findByIdAndUsersId(Long folderId, Long userId);

    List<Share> findAllByUsersId(Long id);
}

