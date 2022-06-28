package com.hanghae99.finalproject.service;

import com.hanghae99.finalproject.model.dto.*;
import com.hanghae99.finalproject.model.entity.*;
import com.hanghae99.finalproject.model.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserService userService;
    private final FolderRepository folderRepository;

    @Transactional(readOnly = true)
    public FolderAndBoardResponseDto findMyFolderAndBoardList(HttpServletRequest request) {
        Users user = userFindByToken(request);
        return new FolderAndBoardResponseDto(
                boardRepository.findByUsersIdAndFolderIdIsNull(user.getId()),
                folderRepository.findByUsersId(user.getId())
        );
    }

    @Transactional
    public Long boardSave(BoardRequestDto boardRequestDto, HttpServletRequest request) {
        Users user = userFindByToken(request);
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
        userAndWriterMatches(board.getUsers().getId(), userFindByToken(request).getId());
        board.update(boardRequestDto);
    }

    @Transactional
    public void boardDelete(Long id, HttpServletRequest request) {
        Board board = boardFindById(id);
        userAndWriterMatches(board.getUsers().getId(), userFindByToken(request).getId());
        boardRepository.deleteById(id);
    }

    public Board boardFindById(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 글을 찾지 못했습니다."));
    }

    private void userAndWriterMatches(Long boardUserId, Long userId) {
        if (boardUserId != userId) {
            throw new RuntimeException("글쓴이가 아닙니다.");
        }
    }

    public Users userFindByToken(HttpServletRequest request) {
        return userService.findUser(request.getAttribute("Authorization").toString());
    }
}
