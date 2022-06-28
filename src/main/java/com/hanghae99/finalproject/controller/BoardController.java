package com.hanghae99.finalproject.controller;

import com.hanghae99.finalproject.model.dto.*;
import com.hanghae99.finalproject.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/board")
    private FolderAndBoardResponseDto findMyFolderAndBoardList(HttpServletRequest request) {
        return boardService.findMyFolderAndBoardList(request);
    }

    @PostMapping("/board")
    public Long boardSave(@RequestBody BoardRequestDto boardRequestDto,
                          HttpServletRequest request) {
        return boardService.boardSave(boardRequestDto, request);
    }

    @PutMapping("/board/{id}")
    public void boardUpdate(@PathVariable Long id,
                            @RequestBody BoardRequestDto boardRequestDto,
                            HttpServletRequest request) {
        boardService.boardUpdate(id, boardRequestDto, request);
    }

    @DeleteMapping("/board/{id}")
    public void boardDelete(@PathVariable Long id,
                            HttpServletRequest request) {
        boardService.boardDelete(id, request);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMassageResponseDto exceptionHandler(Exception e) {
        return new ErrorMassageResponseDto(e.getMessage());
    }
}
