package com.hanghae99.finalproject.controller;

import com.hanghae99.finalproject.model.dto.requestDto.*;
import com.hanghae99.finalproject.model.dto.responseDto.*;
import com.hanghae99.finalproject.model.entity.Board;
import com.hanghae99.finalproject.model.resultType.CategoryType;
import com.hanghae99.finalproject.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import java.util.*;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @PostMapping("/board")
    public MassageResponseDto boardSave(@RequestBody BoardRequestDto boardRequestDto,
                                        HttpServletRequest request) {
        return boardService.boardSave(boardRequestDto, request);
    }

    @PutMapping("/board/{id}")
    public void boardUpdate(@PathVariable Long id,
                            @RequestBody BoardRequestDto boardRequestDto,
                            HttpServletRequest request) {
        boardService.boardUpdate(id, boardRequestDto, request);
    }

    @DeleteMapping("/boards/{folderId}")
    public void boardDelete(@RequestBody List<BoardRequestDto> boardRequestDtos,
                            HttpServletRequest request,
                            @PathVariable Long folderId) {
        boardService.boardDelete(boardRequestDtos, request, folderId);
    }

    @PostMapping("/image/og")
    public OgResponseDto thumbnailLoad(@RequestBody OgRequestDto dto) {
        return boardService.thumbnailLoad(dto.getUrl());
    }

    @PostMapping("/boards")
    public void boardOrderChange(@RequestBody OrderRequestDto orderRequestDto,
                                 HttpServletRequest request) {
        boardService.boardOrderChange(orderRequestDto, request);
    }

    //    @PostMapping("/myshare/board/{boardId}")
    //    public void cloneBoard(@PathVariable Long boardId, HttpServletRequest request) {
    //        boardService.cloneBoard(boardId, request);
    //    }
    @PostMapping("/myshare/boards")
    public void cloneBoards(@RequestBody List<BoardRequestDto> boards, HttpServletRequest request) {
        boardService.cloneBoards(boards, request);
    }

    @GetMapping("/newboards/{page}/{size}")
    public Page<Board> findNewBoards(@PathVariable int page, @PathVariable int size) {
        return boardService.findNewBoard(page, size);
    }

    @PostMapping("/boards/{userId}/{folderId}/{keyword}")
    public FolderRequestDto moum(@RequestBody List<FolderRequestDto> folderRequestDtos,
                                 @PathVariable String keyword,
                                 HttpServletRequest request,
                                 @PageableDefault(size = 20, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable,
                                 @PathVariable Long folderId,
                                 @PathVariable Long userId) {
        return boardService.moum(folderRequestDtos, keyword, request, pageable, folderId, userId);
    }

    @GetMapping("/boards/{userId}/{folderId}")
    public List<Map<String, CategoryType>> findCategoryList(@PathVariable Long folderId,
                                                            @PathVariable Long userId,
                                                            HttpServletRequest request) {
        return boardService.findCategoryList(userId, folderId, request);
    }

    @PostMapping("/allboards/{keyword}/{page}")
    public BoardAndCntResponseDto allBoards(@PathVariable String keyword,
                                            @PathVariable int page,
                                            @RequestBody List<FolderRequestDto> folderRequestDtos,
                                            HttpServletRequest request) {
        return boardService.allBoards(keyword, page, folderRequestDtos, request);
    }
}
