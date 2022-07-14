package com.hanghae99.finalproject.model.dto.requestDto;

import com.hanghae99.finalproject.model.entity.*;
import com.hanghae99.finalproject.util.DisclosureStatus;
import com.hanghae99.finalproject.util.resultType.CategoryType;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
public class FolderRequestDto {
    private Long id;
    private String name;
    private DisclosureStatus status;
    private Long sharedCount;
    private Long folderOrder;
    private CategoryType category;
    private Long boardCnt;
    private List<Board> boardList = new ArrayList<>();

    public FolderRequestDto(Folder folder) {
        this.id = folder.getId();
        this.name = folder.getName();
        this.status = folder.getStatus();
        this.folderOrder = folder.getFolderOrder();
        this.sharedCount = folder.getSharedCount();
    }

    public FolderRequestDto(Page<Board> boards, Folder folder) {
        this.id = folder.getId();
        this.name = folder.getName();
        this.status = folder.getStatus();
        this.boardCnt = folder.getBoardCnt();
        this.sharedCount = folder.getSharedCount();
        this.folderOrder = folder.getFolderOrder();
        this.boardList = boards.getContent();
    }
    public FolderRequestDto(Folder folder, List<Board> boards) {
        this.id = folder.getId();
        this.name = folder.getName();
        this.status = folder.getStatus();
        this.folderOrder = folder.getFolderOrder();
        this.sharedCount = folder.getSharedCount();
        this.boardCnt = folder.getBoardCnt();
    }
}
