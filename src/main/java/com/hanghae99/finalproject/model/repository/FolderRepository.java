package com.hanghae99.finalproject.model.repository;

import com.hanghae99.finalproject.model.entity.*;
import com.hanghae99.finalproject.model.resultType.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;

import java.util.*;

public interface FolderRepository extends JpaRepository<Folder, Long> {

    Optional<Folder> findByIdAndUsersId(Long folderId, Long userId);

    @Query("select new Folder (f.id, f.status) from Folder f where f.id = ?1 and f.users.id = ?2")
    Optional<Folder> findFolderByIdAndUsersId(Long folderId, Long userId);

    void deleteAllByUsers(Users user);

    @Query("select f from Folder f where  f.name not like '무제'   and f.status in ?1")
    Page<Folder> findAllBystatus(DisclosureStatusType status, Pageable pageable);

    Optional<Folder> findByIdAndUsersIdNot(Long folderId, Long id);

    @Query("select count(id) from Folder where users.id = ?1")
    Long findFolderCount(Long id);

    @Query("select distinct(f) from Folder f, Board b where f.users = ?2 and  f.name LIKE case when ?1 = '%all%' then '%%' else ?1 end and f.name not like case when ?3 = false then '무제' else '' end and f.status in ?4 and f.id = b.folder.id and b.category in ?5")
    Page<Folder> findByNameContaining(String keyword,
                                      Users users,
                                      boolean isBoardInBasicFolder,
                                      List<DisclosureStatusType> disclosureStatusTypes,
                                      List<CategoryType> selectCategory,
                                      Pageable pageable);

    @Query("select distinct(f) from Folder f where f.users = ?2 and  f.name LIKE case when ?1 = '%all%' then '%%' else ?1 end and f.name not like case when ?3 = false then '무제' else '' end and f.status in ?4")
    Page<Folder> findByNameContaining(String keyword,
                                      Users users,
                                      boolean isBoardInBasicFolder,
                                      List<DisclosureStatusType> disclosureStatusTypes,
                                      Pageable pageable);

    Folder findByUsersAndName(Users users, String basicFolder);

    @Query("select f from Folder f where f.name LIKE case when ?1 = '%all%' then '%%' else ?1 end and f.name not like '무제' and f.status in ?2 and not f.users.id = ?3")
    Page<Folder> findAllByNameContaining1(String keyword, DisclosureStatusType disclosureStatuses, Long usersId, Pageable pageable);

    Optional<List<Folder>> findAllByIdInAndUsersId(List<Long> folderId, Long id);

    @Query("select f FROM Folder f where f.id In ?1 and f.name LIKE CASE WHEN ?2 = '%all%' then '%%' else ?2 end")
    Page<Folder> findAllByIdAndNameLike(List<Long> listToId, String s, Pageable pageable);

    @Modifying
    @Query("update Folder f set f.folderOrder = f.folderOrder +1 where f.folderOrder < ?1 and ?2 <= f.folderOrder ")
    void updateOrderSum(Long beforeOrder, Long afterOrder);

    @Modifying
    @Query("update Folder f set f.folderOrder = f.folderOrder -1 where f.folderOrder > ?1 and ?2 >= f.folderOrder ")
    void updateOrderMinus(Long beforeOrder, Long afterOrder);

    @Query("select f from Folder f where f.users.id = ?1 and  f.name not like case when ?2 = 'all' then '' else '무제' end and  f.status in ?3")
    List<Folder> findFolderList(Long id, String status, List<DisclosureStatusType> disclosureStatusTypes);
}
