package com.hanghae99.finalproject.model.dto.requestDto;

import com.hanghae99.finalproject.model.dto.responseDto.OgResponseDto;
import com.hanghae99.finalproject.model.entity.Board;
import com.hanghae99.finalproject.util.DisclosureStatus;
import com.hanghae99.finalproject.util.resultType.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class BoardRequestDto {
    private Long id;
    private String title;
    private String link;
    private String explanation;
    private String imgPath;
    private String content;
    private DisclosureStatus status;
    private BoardType boardType;
    private Long folderId;
    private CategoryType category;
    private Long boardOrder;

    public BoardRequestDto(Board board) {
        this.id = board.getId();
        this.link = board.getLink();
        this.title = board.getTitle();
        this.explanation = board.getExplanation();
        this.imgPath = board.getImgPath();
        this.content = board.getContent();
        this.status = board.getStatus();
        this.boardType = board.getBoardType();
        this.category = board.getCategory();
        this.boardOrder = board.getBoardOrder();
    }

    public void ogTagToBoardRequestDto(OgResponseDto ogResponseDto) {
        this.title = ogResponseDto.getTitle();
        this.explanation = ogResponseDto.getDescription();
        this.imgPath = ogResponseDto.getImage();
    }
}
