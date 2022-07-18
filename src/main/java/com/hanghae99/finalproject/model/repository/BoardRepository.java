package com.hanghae99.finalproject.model.repository;

import com.hanghae99.finalproject.controller.MailController;
import com.hanghae99.finalproject.model.entity.*;
import com.hanghae99.finalproject.model.resultType.*;
import com.hanghae99.finalproject.service.MailService;
import com.hanghae99.finalproject.util.mail.MailUtils;
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

    @Query("select b from Board b where  b.status = ?1 and b.title LIKE case when ?2 = '%all%' then '%%' else ?2 end and b.category in ?3")
    Page<Board> findAllByStatusAndTitleContainingAndCategoryIn(DisclosureStatusType status, String keyword, List<CategoryType> categoryTypeList, Pageable pageable);

    @Query("select b from Board b where  b.status = ?1 and b.title LIKE case when ?2 = '%all%' then '%%' else ?2 end and not b.users.id = ?3")
    Page<Board> findAllByStatusAndTitleContaining(DisclosureStatusType status, String keyword,Long usersId ,Pageable pageable);

    List<Board> deleteAllByFolderIdIn(List<Long> folderIdList);

    @Query("SELECT DISTINCT(b.category) FROM Board b WHERE b.users.id = ?1 and b.folder.id = ?2")
    List<CategoryType> findCategoryByUsersIdAndFolderId(Long id, Long folderId);

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
}
