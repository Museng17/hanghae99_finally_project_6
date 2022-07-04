package com.hanghae99.finalproject.util.resultType;

public enum BoardType {
    MEMO("memo"),
    LINK("link");

    private String boardType;

    BoardType(String boardType) {
        this.boardType = boardType;
    }

    public String getBoardType() {
        return boardType;
    }

    public BoardType setBoardType(String boardType) {
        this.boardType = boardType;
        return this;
    }
}
