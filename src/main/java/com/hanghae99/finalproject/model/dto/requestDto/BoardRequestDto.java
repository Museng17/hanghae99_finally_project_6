package com.hanghae99.finalproject.model.dto.requestDto;

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
    private String explanation;
    private String imgPath;
    private String content;
    private DisclosureStatus status;
    private BoardType boardType;
    private Long folderId;
    private CategoryType category;
    private Long order;

    public BoardRequestDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.explanation = board.getExplanation();
        this.imgPath = board.getImgPath();
        this.content = board.getContent();
        this.status = board.getStatus();
        this.boardType = board.getBoardType();
        this.category = board.getCategory();
        this.order = board.getOrder();
    }
}
