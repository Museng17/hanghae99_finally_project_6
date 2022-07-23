package com.hanghae99.finalproject.model.dto.responseDto;

import com.hanghae99.finalproject.model.entity.Board;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
public class BoardAndCntResponseDto {
    private Long boardsCnt;
    private List<Board> boards = new ArrayList<>();

    public BoardAndCntResponseDto(Page<Board> boards, Long boardsCnt) {
        this.boardsCnt = boardsCnt;
        this.boards = boards.getContent();
    }
}
