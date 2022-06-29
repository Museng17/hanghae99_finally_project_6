package com.hanghae99.finalproject.model.repository;

import com.hanghae99.finalproject.model.entity.Folder;
import org.springframework.data.jpa.repository.*;

import java.util.List;

public interface FolderRepository extends JpaRepository<Folder, Long> {
    @Query("select new Folder(f.id, f.name, f.status) from Folder f where f.users.id = ?1")
    List<Folder> findByUsersIdOnlyFolder(Long id);
}
