package com.hanghae99.finalproject.model.repository;

import com.hanghae99.finalproject.model.entity.*;
import org.springframework.data.jpa.repository.*;

import java.util.*;

public interface FolderRepository extends JpaRepository<Folder, Long> {
    @Query("select new Folder(f.id, f.name, f.status, f.category) from Folder f where f.users.id = ?1")
    List<Folder> findByUsersIdOnlyFolder(Long id, String all);

    Optional<Folder> findByIdAndUsersId(Long folderId, Long userId);

    void deleteAllByUsers(Users user);

    @Query("select count(id) from Folder where users.id = ?1")
    Long findFolderCount(Long id);

    List<Folder> findByUsers(Users userFindByToken);
}
