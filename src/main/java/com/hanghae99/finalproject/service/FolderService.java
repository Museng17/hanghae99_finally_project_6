package com.hanghae99.finalproject.service;

import com.hanghae99.finalproject.model.dto.requestDto.FolderRequestDto;
import com.hanghae99.finalproject.model.entity.*;
import com.hanghae99.finalproject.model.repository.FolderRepository;
import com.hanghae99.finalproject.model.repository.ShareRepository;
import com.hanghae99.finalproject.util.UserinfoHttpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FolderService {

    private final FolderRepository folderRepository;
    private final UserinfoHttpRequest userinfoHttpRequest;
    private final BoardService boardService;

    private final ShareRepository shareRepository;

    @Transactional
    public void folderSave(FolderRequestDto folderRequestDto, HttpServletRequest request) {
        folderRepository.save(
                new Folder(
                        folderRequestDto,
                        userinfoHttpRequest.userFindByToken(request)
                )
        );
    }

    @Transactional(readOnly = true)
    public Folder findFolder(Long folderId, HttpServletRequest request) {
        return folderRepository.findByIdAndUsersId(
                        folderId,
                        userinfoHttpRequest.userFindByToken(request).getId()
                )
                .orElseThrow(() -> new RuntimeException("찾는 폴더가 없습니다."));
    }

    @Transactional
    public void boardInFolder(Long folderId, FolderRequestDto folderRequestDto, HttpServletRequest request) {
        Folder folder = findFolder(folderId, request);
        userinfoHttpRequest.userAndWriterMatches(
                folder.getUsers().getId(),
                userinfoHttpRequest.userFindByToken(request).getId());

        List<Board> boardList = boardService.findAllById(
                folderRequestDto.getBoardList().stream()
                        .map(Board::getId)
                        .collect(Collectors.toList())
        );

        folder.boardInFolder(boardList);
        for (Board board : boardList) {
            board.addFolderId(folder);
        }
    }

    @Transactional
    public void folderDelete(Long folderId, HttpServletRequest request) {
        Folder folder = findFolder(folderId, request);
        userinfoHttpRequest.userAndWriterMatches(folder.getUsers().getId(), userinfoHttpRequest.userFindByToken(request).getId());
        boardService.boardDeleteByFolderId(folderId);
        folderRepository.deleteById(folderId);
    }

    @Transactional
    public void folderUpdate(Long folderId, HttpServletRequest request, FolderRequestDto folderRequestDto) {
        Folder folder = findFolder(folderId, request);
        userinfoHttpRequest.userAndWriterMatches(folder.getUsers().getId(), userinfoHttpRequest.userFindByToken(request).getId());
        folder.update(folderRequestDto);
    }
    @Transactional
    public void shareFolder(Long folderId, HttpServletRequest request){
        Users users = userinfoHttpRequest.userFindByToken(request);
        Folder folder = findShareFolder(folderId,request);
        Share share = new Share(folder,users);
        shareRepository.save(share);
    }
    public Folder findShareFolder(Long folderId, HttpServletRequest request){
        return folderRepository.findByIdAndUsersIdNot(folderId,userinfoHttpRequest.userFindByToken(request).getId()).orElseThrow(()
                -> new RuntimeException("원하는 폴더를 찾지 못했습니다."));
    }

}
