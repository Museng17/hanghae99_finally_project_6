package com.hanghae99.finalproject.util.resultType;

import lombok.Getter;
import lombok.NoArgsConstructor;

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
