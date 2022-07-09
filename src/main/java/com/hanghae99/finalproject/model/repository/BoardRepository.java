package com.hanghae99.finalproject.model.repository;

import com.hanghae99.finalproject.model.entity.*;
import com.hanghae99.finalproject.util.DisclosureStatus;
import com.hanghae99.finalproject.util.resultType.CategoryType;
import org.springframework.data.domain.*;
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

    @Query("select b from Board b where b.folder.id = ?1 and b.title LIKE case when ?2 = '%all%' then '%%' else ?2 end ")
    Page<Board> findByFolderIdAndTitleContaining(Long folderId, String keyword, Pageable pageable);

    @Query("select b from Board b where b.folder.id = ?1 and b.title LIKE case when ?2 = '%all%' then '%%' else ?2 end and b.category in ?3")
    Page<Board> findByFolderIdAndTitleContainingAndCategoryIn(Long folderId, String keyword, List<CategoryType> categoryTypeList, Pageable pageable);

    List<Board> findByFolder(Folder folder);
}
