package com.hanghae99.finalproject.controller;

import com.hanghae99.finalproject.jwt.JwtTokenProvider;
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

    @PostMapping("/board/{id}")
    private FolderAndBoardResponseDto findMyFolderAndBoardList(@PathVariable Long id) {
        return boardService.findMyFolderAndBoardList(id);
    }

    @PostMapping("/board")
    public Long boardSave(@RequestBody BoardRequestDto boardRequestDto) {
        return boardService.boardSave(boardRequestDto);
    }

    @PutMapping("/board/{id}")
    public void boardUpdate(@PathVariable Long id,
                            @RequestBody BoardRequestDto boardRequestDto) {
        boardService.boardUpdate(id, boardRequestDto);
    }

    @DeleteMapping("/board/{id}")
    public void boardDelete(@PathVariable Long id) {
        boardService.boardDelete(id);
    }

    @GetMapping("/test")
    public void test() {
        boardService.test();
    }

    @GetMapping("/test2")
    public String test2(HttpServletRequest request) {
        return request.getAttribute("Authorization").toString();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMassageResponseDto exceptionHandler(Exception e) {
        return new ErrorMassageResponseDto(e.getMessage());
    }
}
