package com.hanghae99.finalproject.model.resultType;

import lombok.*;

@Getter
@NoArgsConstructor
public enum BoardType {
    MEMO("memo"),
    LINK("link");

    private String boardType;

    BoardType(String boardType) {
        this.boardType = boardType;
    }

}
