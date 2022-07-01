package com.hanghae99.finalproject.service;

import com.hanghae99.finalproject.model.dto.*;
import com.hanghae99.finalproject.model.entity.*;
import com.hanghae99.finalproject.model.repository.*;
import com.hanghae99.finalproject.util.UserinfoHttpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final FolderRepository folderRepository;
    private final UserinfoHttpRequest userinfoHttpRequest;

    @Transactional(readOnly = true)
    public FolderAndBoardResponseDto findMyFolderAndBoardList(HttpServletRequest request) {
        Users user = userinfoHttpRequest.userFindByToken(request);
        return new FolderAndBoardResponseDto(
                boardRepository.findByUsersIdAndFolderIdIsNull(user.getId()),
                folderRepository.findByUsersIdOnlyFolder(user.getId(), "all")
        );
    }

    @Transactional
    public Long boardSave(BoardRequestDto boardRequestDto, HttpServletRequest request) {
        Users user = userinfoHttpRequest.userFindByToken(request);
        return boardRepository.save(
                new Board(
                        boardRequestDto,
                        user
                )
        ).getId();
    }

    @Transactional
    public void boardUpdate(Long id, BoardRequestDto boardRequestDto, HttpServletRequest request) {
        Board board = boardFindById(id);
        userinfoHttpRequest.userAndWriterMatches(board.getUsers().getId(), userinfoHttpRequest.userFindByToken(request).getId());
        board.update(boardRequestDto);
    }

    @Transactional
    public void boardDelete(Long id, HttpServletRequest request) {
        Board board = boardFindById(id);
        userinfoHttpRequest.userAndWriterMatches(board.getUsers().getId(), userinfoHttpRequest.userFindByToken(request).getId());
        boardRepository.deleteById(id);
    }

    public Board boardFindById(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 글을 찾지 못했습니다."));
    }

    public List<Board> findAllById(List<Long> longs) {
        return boardRepository.findAllById(longs);
    }

    public void boardDeleteByFolderId(Long folderId) {
        boardRepository.deleteByFolderId(folderId);
    }
}
