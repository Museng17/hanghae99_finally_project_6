package com.hanghae99.finalproject.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class ErrorMassageResponseDto {
    private String message;

    public ErrorMassageResponseDto(String message) {
        this.message = message;
    }
}
