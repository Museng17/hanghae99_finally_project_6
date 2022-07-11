package com.hanghae99.finalproject.model.dto.responseDto;

import com.hanghae99.finalproject.model.entity.*;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
public class FolderAndBoardResponseDto {


    private List<Board> boards = new ArrayList<>();
    private List<Folder> folders = new ArrayList<>();


    public FolderAndBoardResponseDto(Page<Board> boards, Page<Folder> folders) {
        this.boards = boards.getContent();
        this.folders = folders.getContent();
    }


}
