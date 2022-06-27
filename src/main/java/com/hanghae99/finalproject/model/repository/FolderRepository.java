package com.hanghae99.finalproject.model.repository;

import com.hanghae99.finalproject.model.entity.Folder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FolderRepository extends JpaRepository <Folder,Long> {
    List<Folder> findByUsersId(Long id);
}
