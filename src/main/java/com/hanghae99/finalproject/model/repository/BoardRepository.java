package com.hanghae99.finalproject.model.repository;

import com.hanghae99.finalproject.model.entity.*;
import com.hanghae99.finalproject.util.DisclosureStatus;
import com.hanghae99.finalproject.util.resultType.CategoryType;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

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

    @Query("select b from Board b where b.folder.id = ?1 and b.title LIKE case when ?2 = '%all%' then '%%' else ?2 end and b.users.id = ?3 and b.status IN ?4")
    Page<Board> findByFolderIdAndTitleContaining(Long folderId, String keyword, Long userId, List<DisclosureStatus> disclosureStatuses, Pageable pageable);

    @Query("select b from Board b where  b.folder.id = ?1 and b.title LIKE case when ?2 = '%all%' then '%%' else ?2 end and b.category in ?3 and b.users.id = ?4 and b.status IN ?5")
    Page<Board> findByFolderIdAndTitleContainingAndCategoryIn(Long folderId,
                                                              String keyword,
                                                              List<CategoryType> categoryTypeList,
                                                              Long userId,
                                                              List<DisclosureStatus> disclosureStatuses,
                                                              Pageable pageable);

    List<Board> findByFolder(Folder folder);

    List<Board> findByUsersId(Long id);

    @Query(value = "SELECT DISTINCT(B.CATEGORY) FROM board B WHERE B.USERS_ID = :id", nativeQuery = true)
    List<CategoryType> findAllCategoryByUsersId(@Param("id") Long id);
}
