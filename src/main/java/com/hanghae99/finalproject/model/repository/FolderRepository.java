package com.hanghae99.finalproject.model.repository;

import com.hanghae99.finalproject.model.entity.*;
import com.hanghae99.finalproject.util.DisclosureStatus;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;

import java.util.*;

public interface FolderRepository extends JpaRepository<Folder, Long> {
    @Query("select new Folder(f.id, f.name, f.status) from Folder f where f.users.id = ?1")
    List<Folder> findByUsersIdOnlyFolder(Long id, String all);

    Optional<Folder> findByIdAndUsersId(Long folderId, Long userId);

    void deleteAllByUsers(Users user);

    Page<Folder> findAllBystatus(DisclosureStatus status, Pageable pageable);

    Optional<Folder> findByIdAndUsersIdNot(Long folderId, Long id);

    @Query("select count(id) from Folder where users.id = ?1")
    Long findFolderCount(Long id);

    List<Folder> findByUsers(Users userFindByToken);

    @Query("select f from Folder f where f.users = ?2 and  f.name LIKE case when ?1 = '%all%' then '%%' else ?1 end and f.name not like case when ?3 = false then '무제' else '' end and f.status in ?4")
    Page<Folder> findByNameContaining(String keyword, Users users, boolean isBoardInBasicFolder, List<DisclosureStatus> disclosureStatuses, Pageable pageable);

    Folder findByUsersAndName(Users users, String basicFolder);
}
