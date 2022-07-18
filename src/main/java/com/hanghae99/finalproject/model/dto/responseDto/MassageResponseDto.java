package com.hanghae99.finalproject.model.dto.responseDto;

import lombok.*;

import java.util.List;

@Getter

@NoArgsConstructor
public class MassageResponseDto<T> {
    private Integer statusCode;
    private String massage;
    private T content;
    private List<T> contentList;

    public MassageResponseDto(Integer statusCode, String massage, T content) {
        this.statusCode = statusCode;
        this.massage = massage;
        this.content = content;
    }

    public MassageResponseDto(int statusCode, String massage) {
        this.statusCode = statusCode;
        this.massage = massage;
    }
}
