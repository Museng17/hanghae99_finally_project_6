package com.hanghae99.finalproject.exceptionHandler;

import com.hanghae99.finalproject.exceptionHandler.CustumException.CustomException;
import com.hanghae99.finalproject.model.dto.responseDto.ErrorMassageResponseDto;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CustomException> customException(CustomException e) {
        CustomException response = new CustomException(e.getMassage(), e.getStatusCode());
        return new ResponseEntity<>(response, HttpStatus.valueOf(e.getStatusCode()));
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

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMassageResponseDto NotTokenHeaderExceptionHandler(MaxUploadSizeExceededException e) {
        return new ErrorMassageResponseDto("파일의 최대 크기는 10MB 입니다.");
    }
}
