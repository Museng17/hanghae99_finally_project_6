package com.hanghae99.finalproject.model.dto;

import com.hanghae99.finalproject.model.entity.*;
import lombok.*;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
public class FolderAndBoardResponseDto {
    private List<BoardRequestDto> boardList = new ArrayList<>();
    private List<FolderRequestDto> folderList = new ArrayList<>();

    public FolderAndBoardResponseDto(List<Board> boards, List<Folder> folders) {
        this.boardList = BoardToBoardRequestDto(boards);
        this.folderList = FolderToFolderRequestDto(folders);
    }

    public List<BoardRequestDto> BoardToBoardRequestDto(List<Board> boards) {
        List<BoardRequestDto> boardRequestDtos = new ArrayList<>();
        for (Board board : boards) {
            boardRequestDtos.add(new BoardRequestDto(board));
        }
        return boardRequestDtos;
    }

    public List<FolderRequestDto> FolderToFolderRequestDto(List<Folder> folders) {
        List<FolderRequestDto> folderRequestDtos = new ArrayList<>();
        for (Folder folder : folders) {
            folderRequestDtos.add(new FolderRequestDto(folder));
        }
        return folderRequestDtos;
    }
}
