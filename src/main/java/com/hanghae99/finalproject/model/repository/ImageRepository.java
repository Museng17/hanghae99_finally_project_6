package com.hanghae99.finalproject.model.repository;

import com.hanghae99.finalproject.model.entity.*;
import com.hanghae99.finalproject.model.resultType.ImageType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    boolean existsByImageTypeAndBoard(ImageType imageType, Board board);

    List<Image> findByBoard(Board board);

    void deleteAllByBoardIdIn(List<Long> longs);
}
