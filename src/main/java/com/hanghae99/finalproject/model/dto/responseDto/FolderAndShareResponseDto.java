package com.hanghae99.finalproject.model.dto.responseDto;

import com.hanghae99.finalproject.model.entity.Folder;
import lombok.*;

import java.util.*;

@Getter
@NoArgsConstructor
public class FolderAndShareResponseDto {

    private List<Folder> foldersList = new ArrayList<>();
    private List<Folder> sharesList = new ArrayList<>();

    public FolderAndShareResponseDto(List<Folder> folders, List<Folder> share) {
        this.foldersList = folders;
        this.sharesList = share;
    }
}
