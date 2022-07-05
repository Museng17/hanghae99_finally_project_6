package com.hanghae99.finalproject.model.repository;

import com.hanghae99.finalproject.model.entity.Folder;
import com.hanghae99.finalproject.model.entity.Users;
import org.springframework.data.jpa.repository.*;

import java.util.*;

public interface FolderRepository extends JpaRepository<Folder, Long> {
    @Query("select new Folder(f.id, f.name, f.status) from Folder f where f.users.id = ?1")
    List<Folder> findByUsersIdOnlyFolder(Long id, String all);

    Optional<Folder> findByIdAndUsersId(Long folderId, Long userId);

    void deleteAllByUsers(Users user);

    Optional<Folder> findByIdAndUsersIdNot(Long folderId, Long Id);
}
