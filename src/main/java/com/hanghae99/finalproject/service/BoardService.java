package com.hanghae99.finalproject.service;

import com.hanghae99.finalproject.model.dto.*;
import com.hanghae99.finalproject.model.entity.*;
import com.hanghae99.finalproject.model.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    //test //토큰으로 교체
    private final UserRepository userRepository;
    private final FolderRepository folderRepository;

    @Transactional(readOnly = true)
    public FolderAndBoardResponseDto findMyFolderAndBoardList(Long id) {
        return new FolderAndBoardResponseDto(
                boardRepository.findByUsersIdAndFolderIdIsNull(id),
                folderRepository.findByUsersId(id)
        );
    }

    @Transactional
    public Long boardSave(BoardRequestDto boardRequestDto) {
        Users user = userRepository.findById(1L).orElseGet(() -> userRepository.save(new Users("test"))); //토큰으로 교체

        return boardRepository.save(
                new Board(
                        boardRequestDto,
                        user
                )
        ).getId();
    }

    @Transactional
    public void boardUpdate(Long id, BoardRequestDto boardRequestDto) {
        boardFindById(id)
                .update(boardRequestDto);
    }

    public void boardDelete(Long id) {
        boardRepository.deleteById(id);
    }

    public Board boardFindById(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 글을 찾지 못했습니다."));
    }

    @Transactional
    public void test() {
        Users user = userRepository.findById(1L).orElseGet(() -> userRepository.save(new Users("test"))); //토큰으로 교체

        Optional<Board> board = boardRepository.findById(1L);

        Folder test = folderRepository.save(
                new Folder(
                        board,
                        user
                )
        );

        board.get().addFolderId(test);
    }
}
