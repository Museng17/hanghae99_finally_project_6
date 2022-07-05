package com.hanghae99.finalproject.service;

import com.hanghae99.finalproject.model.dto.requestDto.BoardRequestDto;
import com.hanghae99.finalproject.model.dto.responseDto.FolderAndBoardResponseDto;
import com.hanghae99.finalproject.model.dto.responseDto.OgResponseDto;
import com.hanghae99.finalproject.model.entity.*;
import com.hanghae99.finalproject.model.repository.*;
import com.hanghae99.finalproject.util.UserinfoHttpRequest;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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
    public Board boardSave(BoardRequestDto boardRequestDto, HttpServletRequest request) {
        Users user = userinfoHttpRequest.userFindByToken(request);
        return boardRepository.save(
                new Board(
                        boardRequestDto,
                        user
                )
        );
    }

    @Transactional
    public void boardUpdate(Long id, BoardRequestDto boardRequestDto, HttpServletRequest request) {
        Board board = boardFindById(id);

        userinfoHttpRequest.userAndWriterMatches(
                board.getUsers().getId(),
                userinfoHttpRequest.userFindByToken(request).getId()
        );

        board.update(boardRequestDto);
    }

    @Transactional
    public void boardDelete(Long id, HttpServletRequest request) {
        Board board = boardFindById(id);

        userinfoHttpRequest.userAndWriterMatches(
                board.getUsers().getId(),
                userinfoHttpRequest.userFindByToken(request).getId()
        );

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

    public OgResponseDto thumbnailLoad(String url){
        OgResponseDto ogResponseDto= new OgResponseDto();
        try{
            Document doc = Jsoup.connect(url).get();
            String title = doc.select("meta[property=og:title]").attr("content");
            String image= doc.select("meta[property=og:image]").attr("content");
            String description = doc.select("meta[property=og:description]").attr("content");
            ogResponseDto= new OgResponseDto(title,image,description);
        }catch(Exception e){
            throw new RuntimeException(e.getMessage());
        }

        return ogResponseDto;

    }
}
