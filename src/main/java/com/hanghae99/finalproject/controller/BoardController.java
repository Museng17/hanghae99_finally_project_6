package com.hanghae99.finalproject.controller;

import com.hanghae99.finalproject.model.dto.requestDto.*;
import com.hanghae99.finalproject.model.dto.responseDto.*;
import com.hanghae99.finalproject.model.entity.Board;
import com.hanghae99.finalproject.model.resultType.CategoryType;
import com.hanghae99.finalproject.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import java.util.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/board/{boardId}")
    public MessageResponseDto findBoard(HttpServletRequest request,
                                        @PathVariable Long boardId) {
        return boardService.findBoard(request, boardId);
    }

    @PostMapping("/board")
    public MessageResponseDto boardSave(@RequestBody BoardRequestDto boardRequestDto,
                                        HttpServletRequest request) {
        return boardService.boardSave(boardRequestDto, request);
    }

    @PutMapping("/board/{id}")
    public MessageResponseDto boardUpdate(@PathVariable Long id,
                            @RequestBody BoardRequestDto boardRequestDto,
                            HttpServletRequest request) {
        boardService.boardUpdate(id, boardRequestDto, request);
        return new MessageResponseDto(200, "수정되었습니다.");
    }

    @DeleteMapping("/boards/{folderId}")
    public MessageResponseDto boardDelete(@RequestBody List<BoardRequestDto> boardRequestDtos,
                            HttpServletRequest request,
                            @PathVariable Long folderId) {
        boardService.boardDelete(boardRequestDtos, request, folderId);
        return new MessageResponseDto(200, "삭제되었습니다.");
    }

    @PostMapping("/image/og")
    public OgResponseDto thumbnailLoad(@RequestBody OgRequestDto dto) {
        return boardService.thumbnailLoad(dto.getUrl());
    }

    @PostMapping("/boards")
    public MessageResponseDto boardOrderChange(@RequestBody OrderRequestDto orderRequestDto,
                                 HttpServletRequest request) {
        boardService.boardOrderChange(orderRequestDto, request);
        return new MessageResponseDto(200, "수정되었습니다.");
    }

    //    @PostMapping("/myshare/board/{boardId}")
    //    public void cloneBoard(@PathVariable Long boardId, HttpServletRequest request) {
    //        boardService.cloneBoard(boardId, request);
    //    }

    @PostMapping("/myshare/boards/{folderId}")
    public MessageResponseDto cloneBoards(@RequestBody List<BoardRequestDto> boards, HttpServletRequest request, @PathVariable Long folderId) {
        return boardService.cloneBoards(boards, request, folderId);
    }

    @GetMapping("/newboards/{page}/{size}")
    public MessageResponseDto findNewBoards(@PathVariable int page, @PathVariable int size, HttpServletRequest request) {
        return boardService.findNewBoard(page, size, request);
    }

    @PostMapping("/boards/{userId}/{folderId}/{keyword}")
    public MessageResponseDto moum(@RequestBody List<FolderRequestDto> folderRequestDtos,
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
    public MessageResponseDto allBoards(@PathVariable String keyword,
                                            @PathVariable int page,
                                            @RequestBody List<FolderRequestDto> folderRequestDtos,
                                            HttpServletRequest request) {
        return boardService.allBoards(keyword, page, folderRequestDtos, request);
    }

    @PostMapping("/folder/{folderId}")
    public void boardInFolder(@PathVariable Long folderId,
                              @RequestBody FolderRequestDto folderRequestDto,
                              HttpServletRequest request) {
        boardService.boardInFolder(folderId, folderRequestDto, request);
    }

    @PutMapping("/board/status")
    public BoardResponseDto updateStatus(@RequestBody BoardRequestDto boardRequestDto,
                              HttpServletRequest request) {
        return boardService.updateStatus(boardRequestDto, request);
    }
    @PostMapping("/reportboard/{boardId}")
    public void reportBoard(@PathVariable Long boardId,HttpServletRequest request){
        boardService.reportBoard(boardId,request);
    }
}
