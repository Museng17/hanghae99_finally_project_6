package com.hanghae99.finalproject.model.dto.responseDto;

import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
public class MessageResponseDto<T> {
    private int statusCode;
    private String message;
    private T content;
    private List<T> contentList;

    public MessageResponseDto(int statusCode, String message, T content) {
        this.statusCode = statusCode;
        this.message = message;
        this.content = content;
    }

    public MessageResponseDto(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}
