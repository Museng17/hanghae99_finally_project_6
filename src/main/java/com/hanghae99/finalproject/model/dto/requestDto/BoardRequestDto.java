package com.hanghae99.finalproject.model.dto.requestDto;

import com.hanghae99.finalproject.model.dto.responseDto.OgResponseDto;
import com.hanghae99.finalproject.model.resultType.*;
import lombok.*;

@Getter
@NoArgsConstructor
public class BoardRequestDto {
    private Long id;
    private String title;
    private String link;
    private String explanation;
    private String imgPath;
    private String content;
    private DisclosureStatusType status;
    private BoardType boardType;
    private Long folderId;
    private CategoryType category;
    private Long boardOrder;

    public void ogTagToBoardRequestDto(OgResponseDto ogResponseDto, String link) {
        if (ogResponseDto.getTitle().equals("")) {
            this.title = link;
        } else {
            this.title = ogResponseDto.getTitle();
        }
        this.explanation = ogResponseDto.getDescription();
        this.imgPath = ogResponseDto.getImage();
    }

    public void updateTitle(String format) {
        this.title = format;
    }

    public void updateImagePath(String url) {
        this.imgPath = url;
    }
}
