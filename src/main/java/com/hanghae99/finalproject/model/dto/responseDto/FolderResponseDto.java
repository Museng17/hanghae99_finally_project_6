package com.hanghae99.finalproject.model.dto.responseDto;

import com.hanghae99.finalproject.model.dto.requestDto.FolderRequestDto;
import lombok.*;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
public class FolderResponseDto {

    private Long foldersCnt;
    private List<FolderRequestDto> folders = new ArrayList<>();

    public FolderResponseDto(List<FolderRequestDto> folders, Long foldersCnt) {
        this.foldersCnt = foldersCnt;
        this.folders = folders;
    }
}
