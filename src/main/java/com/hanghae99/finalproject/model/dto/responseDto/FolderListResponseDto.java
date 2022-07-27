package com.hanghae99.finalproject.model.dto.responseDto;

import lombok.*;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
public class FolderListResponseDto {

    private Long foldersCnt;
    private int totalPages;
    private List<FolderResponseDto> folders = new ArrayList<>();

    public FolderListResponseDto(List<FolderResponseDto> folders, Long foldersCnt) {
        this.foldersCnt = foldersCnt;
        this.folders = folders;
    }
}
