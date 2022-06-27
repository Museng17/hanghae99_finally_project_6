package com.hanghae99.finalproject.model.repository;

import com.hanghae99.finalproject.model.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository <Board, Long> {
}
