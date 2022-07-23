package com.hanghae99.finalproject.model.dto.responseDto;

import com.hanghae99.finalproject.model.dto.requestDto.FolderRequestDto;
import lombok.*;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
public class FolderListResponseDto {

    private Long foldersCnt;
    private List<FolderResponseDto> folders = new ArrayList<>();

    public FolderListResponseDto(List<FolderResponseDto> folders, Long foldersCnt) {
        this.foldersCnt = foldersCnt;
        this.folders = folders;
    }
}
