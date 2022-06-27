package com.hanghae99.finalproject.model.dto;

import com.hanghae99.finalproject.model.entity.*;
import com.hanghae99.finalproject.util.DisclosureStatus;
import lombok.*;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
public class FolderRequestDto {
    private Long id;
    private String name;
    private DisclosureStatus status;
    private List<Board> board = new ArrayList<>();

    public FolderRequestDto(Folder folder) {
        this.id = folder.getId();
        this.name = folder.getName();
        this.status = folder.getStatus();
        this.board = folder.getBoard();
    }
}
