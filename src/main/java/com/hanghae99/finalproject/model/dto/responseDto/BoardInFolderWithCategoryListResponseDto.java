package com.hanghae99.finalproject.model.dto.responseDto;

import com.hanghae99.finalproject.model.dto.requestDto.FolderRequestDto;
import com.hanghae99.finalproject.model.entity.*;
import com.hanghae99.finalproject.util.resultType.CategoryType;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.*;

@Getter
@NoArgsConstructor
public class BoardInFolderWithCategoryListResponseDto {

    private FolderRequestDto folder;
    private List<Map<String, CategoryType>> categoryList;

    public BoardInFolderWithCategoryListResponseDto(Page<Board> byFolderIdAndTitleContainingAndCategoryIn,
                                                    Folder folder,
                                                    List<Map<String, CategoryType>> categoryList) {
        this.folder = new FolderRequestDto(byFolderIdAndTitleContainingAndCategoryIn, folder);
        this.categoryList = categoryList;
    }
}
