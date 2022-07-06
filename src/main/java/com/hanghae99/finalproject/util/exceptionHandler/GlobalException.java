package com.hanghae99.finalproject.util.exceptionHandler;

import com.hanghae99.finalproject.model.dto.responseDto.ErrorMassageResponseDto;
import io.jsonwebtoken.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorMassageResponseDto ExpiredJwtExceptionException() {
        return new ErrorMassageResponseDto("토큰의 유효시간이 만료되었습니다.");
    }

    @ExceptionHandler(JwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorMassageResponseDto JwtExceptionException() {
        return new ErrorMassageResponseDto("변질 된 토큰입니다.");
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMassageResponseDto RuntimeExceptionHandler(Exception e) {
        return new ErrorMassageResponseDto(e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMassageResponseDto IllegalArgumentExceptionHandler(Exception e) {
        return new ErrorMassageResponseDto(e.getMessage());
    }
}