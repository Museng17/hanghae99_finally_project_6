package com.hanghae99.finalproject.model.repository;

import com.hanghae99.finalproject.model.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface ShareRepository extends JpaRepository<Share, Long> {
    Optional<Share> findByFolderIdAndUsersId(Long folderId, Long userId);

    List<Share> findAllByUsersId(Long id);

    void deleteAllByFolderIdIn(List<Long> dbLongList);

    void deleteByUsersId(Long id);

    void deleteAllByFolderIn(List<Folder> folders);
}

