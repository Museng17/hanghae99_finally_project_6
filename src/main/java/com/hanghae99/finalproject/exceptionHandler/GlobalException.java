package com.hanghae99.finalproject.exceptionHandler;

import com.hanghae99.finalproject.exceptionHandler.CustumException.NotTokenHeaderException;
import com.hanghae99.finalproject.model.dto.responseDto.ErrorMassageResponseDto;
import io.jsonwebtoken.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(HttpStatus.GONE)
    public ErrorMassageResponseDto ExpiredJwtExceptionException() {
        return new ErrorMassageResponseDto("토큰의 유효시간이 만료되었습니다.");
    }

    @ExceptionHandler(JwtException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ErrorMassageResponseDto JwtExceptionException() {
        return new ErrorMassageResponseDto("변질 된 토큰입니다.");
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMassageResponseDto RuntimeExceptionHandler(RuntimeException e) {
        return new ErrorMassageResponseDto(e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMassageResponseDto IllegalArgumentExceptionHandler(IllegalArgumentException e) {
        return new ErrorMassageResponseDto(e.getMessage());
    }

    @ExceptionHandler(NotTokenHeaderException.class)
    @ResponseStatus(HttpStatus.PAYMENT_REQUIRED)
    public ErrorMassageResponseDto NotTokenHeaderExceptionHandler(NotTokenHeaderException e) {
        return new ErrorMassageResponseDto(e.getMessage());
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMassageResponseDto NotTokenHeaderExceptionHandler(MaxUploadSizeExceededException e) {
        return new ErrorMassageResponseDto("파일의 최대 크기는 10MB 입니다.");
    }
}
