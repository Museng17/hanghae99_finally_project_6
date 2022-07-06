package com.hanghae99.finalproject.model.repository;

import com.hanghae99.finalproject.model.entity.*;
import org.springframework.data.jpa.repository.*;

import java.util.*;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findByUsersIdAndFolderIdIsNull(Long id);

    void deleteByFolderId(Long folderId);

    Optional<Board> findByFolderId(Long folderId);

    void deleteAllByUsers(Users user);

    @Query("select count(id) from Board where users.id = ?1")
    Long findBoardCount(Long id);

    List<Board> findByUsers(Users user);
}
