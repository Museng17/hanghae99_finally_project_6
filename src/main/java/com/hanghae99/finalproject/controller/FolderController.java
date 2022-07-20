package com.hanghae99.finalproject.controller;

import com.hanghae99.finalproject.model.dto.requestDto.*;
import com.hanghae99.finalproject.model.dto.responseDto.FolderResponseDto;
import com.hanghae99.finalproject.model.entity.*;
import com.hanghae99.finalproject.model.resultType.CategoryType;
import com.hanghae99.finalproject.service.FolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import java.util.*;

@RestController
@RequiredArgsConstructor
public class FolderController {

    private final FolderService folderService;

    @PostMapping("/folder")
    public Folder folderSave(@RequestBody FolderRequestDto folderRequestDto,
                             HttpServletRequest request) {
        return folderService.folderSave(folderRequestDto, request);
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
    public Board crateBoardInFolder(@RequestBody BoardRequestDto boardRequestDto,
                                    HttpServletRequest request) {
        return folderService.crateBoardInFolder(boardRequestDto, request);
    }

    @PostMapping("/folders")
    public void folderOrderChange(@RequestBody OrderRequestDto orderRequestDto,
                                  HttpServletRequest request) {
        folderService.folderOrderChange(orderRequestDto, request);
    }

    @GetMapping("/folders")
    public List<Folder> folders() {
        return folderService.folders();
    }

    @PostMapping("/share/folder/{folderId}")
    public void shareFolder(@PathVariable Long folderId, HttpServletRequest request) {
        folderService.shareFolder(folderId, request);
    }

//    @PostMapping("/myshare/folder/{folderId}")
//    public void cloneFolder(@PathVariable Long folderId, HttpServletRequest request) {
//        folderService.cloneFolder(folderId, request);
//    }

    @GetMapping("/BestFolders/{page}/{size}")
    public Page<Folder> findBestFolders(@PathVariable int page, @PathVariable int size) {
        return folderService.findBestFolder(page, size);
    }

    @PostMapping("/folders/{userId}/{keyword}")
    public List<Folder> moum(@PathVariable String keyword,
                             HttpServletRequest request,
                             @PageableDefault(size = 20, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable,
                             @PathVariable Long userId,
                             @RequestBody List<FolderRequestDto> folderRequestDtos) {
        return folderService.moum(keyword, request, pageable, userId, folderRequestDtos);
    }

    @GetMapping("/folders/{userId}")
    public List<Map<String, CategoryType>> findCategoryList(@PathVariable Long userId,
                                                            HttpServletRequest request) {
        return folderService.findCategoryList(userId, request);
    }

    @GetMapping("/shares/{userId}/{keyword}")
    public List<Folder> shareList(@PathVariable String keyword,
                                  HttpServletRequest request,
                                  @PageableDefault(size = 20, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable,
                                  @PathVariable Long userId) {
        return folderService.shareList(keyword, request, pageable, userId);
    }


    @PostMapping("/allfolders/{keyword}")
    public FolderResponseDto allFolders(@PathVariable String keyword,
                                        HttpServletRequest request,
                                        @PageableDefault(size = 8, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        return folderService.allFolders(keyword,request,pageable);

    }
    @PostMapping("/report/{folderid}")
    public void reportFolder(@PathVariable Long folderId,
                             HttpServletRequest request){
        folderService.reportFolder(folderId,request);
    }
}
