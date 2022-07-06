package com.hanghae99.finalproject.model.dto.requestDto;

import lombok.*;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
public class FolderAndBoardRequestDto {
    private List<BoardRequestDto> boardList = new ArrayList<>();
    private List<FolderRequestDto> folderList = new ArrayList<>();
}
