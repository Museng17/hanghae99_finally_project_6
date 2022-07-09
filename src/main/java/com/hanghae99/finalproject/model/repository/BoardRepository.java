package com.hanghae99.finalproject.model.repository;

import com.hanghae99.finalproject.model.entity.*;
import com.hanghae99.finalproject.util.DisclosureStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.*;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findByUsersIdAndFolderIdIsNull(Long id);

    void deleteByFolderId(Long folderId);

    Optional<Board> findByFolderId(Long folderId);
    Page<Board> findAllByStatus(DisclosureStatus status, Pageable pageable);

    void deleteAllByUsers(Users user);
    Optional<Board> findByIdAndUsersIdNot(Long boardId, Long id);

    @Query("select count(id) from Board where users.id = ?1")
    Long findBoardCount(Long id);

    List<Board> findByUsers(Users user);
}
