package com.hanghae99.finalproject.model.dto.responseDto;

import lombok.*;

@Getter
@NoArgsConstructor
public class MessageResponseDto<T> {
    private int statusCode;
    private String message;
    private int totalPages;
    private T content;

    public MessageResponseDto(int statusCode, String message, T content) {
        this.statusCode = statusCode;
        this.message = message;
        this.content = content;
    }

    public MessageResponseDto(int statusCode, String message, T content, int totalPages) {
        this.statusCode = statusCode;
        this.message = message;
        this.content = content;
        this.totalPages = totalPages;
    }

    public MessageResponseDto(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}
