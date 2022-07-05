package com.hanghae99.finalproject.model.repository;

import com.hanghae99.finalproject.model.entity.Board;
import com.hanghae99.finalproject.model.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findByUsersIdAndFolderIdIsNull(Long id);

    void deleteByFolderId(Long folderId);

    Optional<Board> findByFolderId(Long folderId);

    void deleteAllByUsers(Users user);
}
