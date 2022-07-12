package com.hanghae99.finalproject.controller;

import com.hanghae99.finalproject.model.dto.requestDto.*;
import com.hanghae99.finalproject.model.dto.responseDto.FolderAndBoardResponseDto;
import com.hanghae99.finalproject.model.entity.Folder;
import com.hanghae99.finalproject.service.FolderService;
import com.hanghae99.finalproject.util.resultType.CategoryType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FolderController {

    private final FolderService folderService;

    @PostMapping("/folder")
    public void folderSave(@RequestBody FolderRequestDto folderRequestDto,
                           HttpServletRequest request) {
        folderService.folderSave(folderRequestDto, request);
    }

    @PostMapping("/folder/{folderId}")
    public void boardInFolder(@PathVariable Long folderId,
                              @RequestBody FolderRequestDto folderRequestDto,
                              HttpServletRequest request) {
        folderService.boardInFolder(folderId, folderRequestDto, request);
    }

    @DeleteMapping("/folders")
    public void folderDelete(@RequestBody List<FolderRequestDto> folderRequestDto,
                             HttpServletRequest request) {
        folderService.folderDelete(folderRequestDto, request);
    }

    @PutMapping("/folder/{folderId}")
    public void folderUpdate(@PathVariable Long folderId,
                             @RequestBody FolderRequestDto folderRequestDto,
                             HttpServletRequest request) {
        folderService.folderUpdate(folderId, request, folderRequestDto);
    }

    @PutMapping("/folder")
    public void crateBoardInFolder(@RequestBody BoardRequestDto boardRequestDto,
                                   HttpServletRequest request) {
        folderService.crateBoardInFolder(boardRequestDto, request);
    }

    @PostMapping("/folders")
    public void folderOrderChange(@RequestBody FolderAndBoardRequestDto folderAndBoardRequestDto,
                                  HttpServletRequest request) {
        folderService.folderOrderChange(folderAndBoardRequestDto, request);
    }

    @GetMapping("/folders")
    public List<Folder> folders() {
        return folderService.folders();
    }

    @PostMapping("/share/folder/{folderId}")
    public void shareFolder(@PathVariable Long folderId, HttpServletRequest request) {
        folderService.shareFolder(folderId, request);
    }

    @PostMapping("/myshare/folder/{folderId}")
    public void cloneFolder(@PathVariable Long folderId, HttpServletRequest request) {
        folderService.cloneFolder(folderId, request);

    }

    @GetMapping("/BestFolders/{page}/{size}")
    public Page<Folder> findBestFolders(@PathVariable int page, @PathVariable int size) {
        return folderService.findBestFolder(page, size);
    }

    @PostMapping("/board/{userId}/{keyword}")
    public void YourPage(@PathVariable String keyword,
                         @RequestBody List<CategoryType> categoryTypeList,
                         @PathVariable String userId) {
    }

    @PostMapping("/folders/{userId}/{keyword}")
    public List<Folder> moum(@PathVariable String keyword,
                             HttpServletRequest request,
                             @PageableDefault(size = 20, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable,
                             @PathVariable Long userId) {
        return folderService.moum(keyword, request, pageable, userId);
    }

    @PostMapping("/allfolders/{keyword}/{page}")
    public FolderAndBoardResponseDto allmoum(@PathVariable String keyword, @PathVariable int page, @RequestBody List<FolderRequestDto> folderRequestDtos) {
        return folderService.allmoum(keyword, page, folderRequestDtos);
    }
}
