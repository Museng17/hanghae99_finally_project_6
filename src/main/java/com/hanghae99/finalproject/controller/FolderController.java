package com.hanghae99.finalproject.controller;

import com.hanghae99.finalproject.model.dto.requestDto.*;
import com.hanghae99.finalproject.model.entity.Folder;
import com.hanghae99.finalproject.service.FolderService;
import lombok.RequiredArgsConstructor;
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

    @GetMapping("/folder/{folderId}")
    public Folder findFolder(@PathVariable Long folderId, HttpServletRequest request) {
        return folderService.findFolder(folderId, request);
    }

    @PostMapping("/folder/{folderId}")
    public void boardInFolder(@PathVariable Long folderId,
                              @RequestBody FolderRequestDto folderRequestDto,
                              HttpServletRequest request) {
        folderService.boardInFolder(folderId, folderRequestDto, request);
    }

    @DeleteMapping("/folder/{folderId}")
    public void folderDelete(@PathVariable Long folderId,
                             HttpServletRequest request) {
        folderService.folderDelete(folderId, request);
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
}
