package com.hanghae99.finalproject.model.dto.responseDto;

import com.hanghae99.finalproject.model.dto.requestDto.FolderRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class FolderResponseDto {

    private Long foldersCnt;
//    private List<Folder> folders = new ArrayList<>();
    private List<FolderRequestDto> folders = new ArrayList<>();

    public FolderResponseDto(List<FolderRequestDto> folders, Long foldersCnt){
        this.foldersCnt = foldersCnt;
        this.folders = folders;
    }
}
