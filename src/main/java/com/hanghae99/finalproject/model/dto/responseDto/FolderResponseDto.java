package com.hanghae99.finalproject.model.dto.responseDto;

import com.hanghae99.finalproject.model.entity.Folder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class FolderResponseDto {

    private Long foldersCnt;
    private List<Folder> folders = new ArrayList<>();

    public FolderResponseDto(Page<Folder> folders, Long foldersCnt){
        this.foldersCnt = foldersCnt;
        this.folders = folders.getContent();
    }
}
