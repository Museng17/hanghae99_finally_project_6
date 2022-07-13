package com.hanghae99.finalproject.model.dto.responseDto;

import com.hanghae99.finalproject.model.entity.*;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
public class FolderAndBoardResponseDto {
    private int boardsCnt;
    private List<Board> boards = new ArrayList<>();
    private int foldersCnt;
    private List<Folder> folders = new ArrayList<>();


    public FolderAndBoardResponseDto(Page<Board> boards,int boardsCnt, Page<Folder> folders,int foldersCnt) {
        this.boardsCnt = boardsCnt;
        this.boards = boards.getContent();
        this.foldersCnt = foldersCnt;
        this.folders = folders.getContent();
    }

}
