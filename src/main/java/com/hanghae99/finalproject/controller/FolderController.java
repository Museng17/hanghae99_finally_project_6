package com.hanghae99.finalproject.controller;

import com.hanghae99.finalproject.model.dto.requestDto.*;
import com.hanghae99.finalproject.model.dto.responseDto.*;
import com.hanghae99.finalproject.model.entity.Folder;
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

    @DeleteMapping("/folders")
    public MessageResponseDto folderDelete(@RequestBody List<FolderRequestDto> folderRequestDto,
                             HttpServletRequest request) {
        folderService.folderDelete(folderRequestDto, request);
        return new MessageResponseDto(200, "삭제되었습니다.");
    }

    @PutMapping("/folder/{folderId}")
    public MessageResponseDto folderUpdate(@PathVariable Long folderId,
                             @RequestBody FolderRequestDto folderRequestDto,
                             HttpServletRequest request) {
        folderService.folderUpdate(folderId, request, folderRequestDto);
        return new MessageResponseDto(200, "수정되었습니다.");
    }

    @PutMapping("/folder")
    public MessageResponseDto crateBoardInFolder(@RequestBody BoardRequestDto boardRequestDto,
                                                 HttpServletRequest request) {
        return folderService.crateBoardInFolder(boardRequestDto, request);
    }

    @PostMapping("/folders")
    public MessageResponseDto folderOrderChange(@RequestBody OrderRequestDto orderRequestDto,
                                  HttpServletRequest request) {
        folderService.folderOrderChange(orderRequestDto, request);
        return new MessageResponseDto(200, "수정되었습니다.");
    }

    @PostMapping("/share/folder/{folderId}")
    public MessageResponseDto shareFolder(@PathVariable Long folderId, HttpServletRequest request) {
        return folderService.shareFolder(folderId, request);
    }

    //    @PostMapping("/myshare/folder/{folderId}")
    //    public void cloneFolder(@PathVariable Long folderId, HttpServletRequest request) {
    //        folderService.cloneFolder(folderId, request);
    //    }

    @GetMapping("/BestFolders/{page}/{size}")
    public MessageResponseDto findBestFolders(@PathVariable int page, @PathVariable int size,HttpServletRequest request) {
        return folderService.findBestFolder(page, size,request);
    }

    @PostMapping("/folders/{userId}/{keyword}")
    public MessageResponseDto moum(@PathVariable String keyword,
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
    public MessageResponseDto shareList(@PathVariable String keyword,
                                        HttpServletRequest request,
                                        @PageableDefault(size = 20, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable,
                                        @PathVariable Long userId) {
        return folderService.shareList(keyword, request, pageable, userId);
    }

    @PostMapping("/allfolders/{keyword}")
    public MessageResponseDto allFolders(@PathVariable String keyword,
                                            HttpServletRequest request,
                                            @PageableDefault(size = 8, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        return folderService.allFolders(keyword, request, pageable);

    }

    @PostMapping("/reportfolder/{folderId}")
    public MessageResponseDto reportFolder(@PathVariable Long folderId,
                             HttpServletRequest request) {
        return folderService.reportFolder(folderId, request);
    }

    @GetMapping("/folders")
    public List<FolderResponseDto> findAllFolderList(@RequestParam(value = "status", defaultValue = "all") String status,
                                                     @RequestParam(value = "userId", defaultValue = "0") Long userId,
                                                     HttpServletRequest request) {
        return folderService.findAllFolderList(status, userId, request);
    }

    @PutMapping("/folder/status")
    public Folder updateStatus(@RequestBody FolderRequestDto folderRequestDto,
                               HttpServletRequest request) {
        return folderService.updateStatus(folderRequestDto, request);
    }
    @DeleteMapping("/share/delete/{folderId}")
    public MessageResponseDto deleteShare(@PathVariable Long folderId, HttpServletRequest request) {
        return folderService.deleteShare(folderId, request);
    }
}
