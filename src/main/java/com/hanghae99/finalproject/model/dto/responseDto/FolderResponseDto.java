package com.hanghae99.finalproject.model.dto.responseDto;

import com.hanghae99.finalproject.model.entity.*;
import com.hanghae99.finalproject.model.resultType.DisclosureStatusType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.*;

@Getter
@NoArgsConstructor
public class FolderResponseDto {
    private Long id;
    private String name;
    private DisclosureStatusType status;
    private Long sharedCount;
    private Long folderOrder;
    private Long boardCnt;
    private Long userId;
    private LocalDateTime createDate;
    private LocalDateTime modifiedDate;
    private List<Board> boardList = new ArrayList<>();

    public FolderResponseDto(Folder folder) {
        this.id = folder.getId();
        this.name = folder.getName();
        this.status = folder.getStatus();
        this.sharedCount = folder.getSharedCount();
        this.folderOrder = folder.getFolderOrder();
        this.boardCnt = folder.getBoardCnt();
        this.userId = folder.getUsers().getId();
        this.createDate = folder.getCreatedDate();
        this.modifiedDate = folder.getModifiedDate();
    }
}
