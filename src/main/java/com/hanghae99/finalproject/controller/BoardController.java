package com.hanghae99.finalproject.controller;

import com.hanghae99.finalproject.model.dto.requestDto.*;
import com.hanghae99.finalproject.model.dto.responseDto.*;
import com.hanghae99.finalproject.model.entity.Board;
import com.hanghae99.finalproject.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final S3Uploader s3Uploader;

    @GetMapping("/board")
    private FolderAndBoardResponseDto findMyFolderAndBoardList(HttpServletRequest request) {
        return boardService.findMyFolderAndBoardList(request);
    }

    @PostMapping("/board")
    public Board boardSave(@RequestBody BoardRequestDto boardRequestDto,
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

    @PostMapping("/image/og")
    public OgResponseDto thumbnailLoad(@RequestBody OgRequestDto dto) {

        return boardService.thumbnailLoad(dto.getUrl());
    }

    @PostMapping("/boards")
    public void boardOrderChange(@RequestBody FolderAndBoardRequestDto folderAndBoardRequestDto,
                                 HttpServletRequest request) {
        boardService.boardOrderChange(folderAndBoardRequestDto, request);
    }

    @PostMapping("/myshare/board/{boardId}")
    public void cloneBoard(@PathVariable Long boardId, HttpServletRequest request) {
        boardService.cloneBoard(boardId, request);
    }

    @PostMapping("/board/image")
    public FileUploadResponse boardImageUpload(@RequestParam("boardImage") MultipartFile imageFile) {
        return boardService.boardImageUpload(imageFile);
    }

    @GetMapping("/newboards/{page}/{size}")
    public Page<Board> findNewBoards(@PathVariable int page, @PathVariable int size){
        return boardService.findNewBoard(page, size);
    }
}
