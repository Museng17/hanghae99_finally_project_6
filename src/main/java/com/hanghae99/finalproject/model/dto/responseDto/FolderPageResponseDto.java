package com.hanghae99.finalproject.model.dto.responseDto;

import com.hanghae99.finalproject.model.entity.Folder;
import com.hanghae99.finalproject.util.resultType.CategoryType;
import lombok.*;

import java.util.*;

@Getter
@NoArgsConstructor
public class FolderPageResponseDto {

    private List<Folder> folderList = new ArrayList<>();
    private List<Map<String, CategoryType>> categoryList;

    public FolderPageResponseDto(List<Folder> content, List<Map<String, CategoryType>> categoryByUsersId) {
        this.folderList = content;
        this.categoryList = categoryByUsersId;
    }
}
