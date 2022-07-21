package com.hanghae99.finalproject.model.repository;

import com.hanghae99.finalproject.model.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
