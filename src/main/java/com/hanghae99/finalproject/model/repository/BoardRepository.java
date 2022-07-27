package com.hanghae99.finalproject.model.repository;

import com.hanghae99.finalproject.model.entity.*;
import com.hanghae99.finalproject.model.resultType.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.*;

public interface BoardRepository extends JpaRepository<Board, Long> {

    Optional<Board> findByFolderId(Long folderId);

    Page<Board> findAllByStatus(DisclosureStatusType status, Pageable pageable);

    void deleteAllByUsers(Users user);

    //    Optional<Board> findByIdAndUsersIdNot(Long boardId, Long id);

    @Query("select b from Board b where  b.folder.id = ?1 and b.title LIKE case when ?2 = '%all%' then '%%' else ?2 end and b.category in ?3 and b.users.id = ?4 and b.status IN ?5")
    Page<Board> findByFolderIdAndTitleContainingAndCategoryIn(Long folderId,
                                                              String keyword,
                                                              List<CategoryType> categoryTypeList,
                                                              Long userId,
                                                              List<DisclosureStatusType> disclosureStatusTypes,
                                                              Pageable pageable);

    List<Board> findByFolder(Folder folder);

    @Query("SELECT DISTINCT(B.category) FROM Board B WHERE B.users.id = :id")
    List<CategoryType> findAllCategoryByUsersId(@Param("id") Long id);

    @Query("select b from Board b where  not b.users.id = ?1 and b.status = ?2 and b.title LIKE case when ?3 = '%all%' then '%%' else ?3 end and b.category in ?4")
    Page<Board> findAllByStatusAndTitleContainingAndCategoryIn(Long usersId,
                                                               DisclosureStatusType status,
                                                               String keyword,
                                                               List<CategoryType> categoryTypeList,
                                                               Pageable pageable);

    @Query("select b from Board b where  not b.users.id = ?1 and b.status = ?2 and b.title LIKE case when ?3 = '%all%' then '%%' else ?3 end")
    Page<Board> findAllByStatusAndTitleContaining(Long usersId, DisclosureStatusType status, String keyword, Pageable pageable);

    List<Board> deleteAllByFolderIdIn(List<Long> folderIdList);

    @Query("SELECT DISTINCT(b.category) FROM Board b WHERE b.users.id = ?1 and b.folder.id = ?2 and b.status in ?3")
    List<CategoryType> findCategoryByUsersIdAndFolderId(Long id,
                                                        Long folderId,
                                                        List<DisclosureStatusType> disclosureStatusTypes);

    @Modifying
    @Query("update Board b set b.boardOrder = b.boardOrder +1 where b.boardOrder < ?1 and ?2 <= b.boardOrder and  b.folder.id = ?3")
    void updateOrderSum(Long beforeOrder, Long afterOrder, Long folderId);

    @Modifying
    @Query("update Board b set b.boardOrder = b.boardOrder -1 where b.boardOrder > ?1 and ?2 >= b.boardOrder and  b.folder.id = ?3")
    void updateOrderMinus(Long beforeOrder, Long afterOrder, Long folderId);

    @Query("select b from Board b where b.users.id = ?1 and b.folder.id = ?2 order by b.boardOrder asc")
    List<Board> findAllByUsersIdOrderByBoardOrderAsc(Long boardIdList, Long folderId);

    @Query("select b.folder.id from Board b where b.id = ?1")
    Optional<Long> findFolderIdById(Long id);

    List<Board> findAllByIdIn(List<Long> longList);

    Optional<Board> findByIdAndUsers(Long boardId, Users userFindByToken);

    @Query("select new Board (b.id, b.status) from Board b where b.id = ?1 and b.users.id = ?2")
    Optional<Board> findBoardByIdAndUsersId(Long folderId, Long id);
}
