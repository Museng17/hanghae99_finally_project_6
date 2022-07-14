package com.hanghae99.finalproject.model.dto.responseDto;

import com.hanghae99.finalproject.model.entity.Board;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BoardResponseDto {
    private int boardsCnt;
    private List<Board> boards = new ArrayList<>();

    public BoardResponseDto(Page<Board> boards, int boardsCnt){
        this.boardsCnt = boardsCnt;
        this.boards = boards.getContent();
    }
}
