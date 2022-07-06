package com.hanghae99.finalproject.controller;

import com.hanghae99.finalproject.model.dto.requestDto.BoardRequestDto;
import com.hanghae99.finalproject.model.dto.requestDto.OgRequestDto;
import com.hanghae99.finalproject.model.dto.responseDto.ErrorMassageResponseDto;
import com.hanghae99.finalproject.model.dto.responseDto.FolderAndBoardResponseDto;
import com.hanghae99.finalproject.model.dto.responseDto.OgResponseDto;
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
        return boardService.boardSave(boardRequestDto, request).getId();
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

    @PostMapping("/image/og")
    public OgResponseDto thumbnailLoad(@RequestBody OgRequestDto dto)  {

        return boardService.thumbnailLoad(dto.getUrl());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMassageResponseDto exceptionHandler(Exception e) {
        return new ErrorMassageResponseDto(e.getMessage());
    }

    @PostMapping("/myshare/board/{boardId}")
    public void cloneBoard(@PathVariable Long boardId,HttpServletRequest request){
        boardService.cloneBoard(boardId,request);
    }
}
