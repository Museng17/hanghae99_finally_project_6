package com.hanghae99.finalproject.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hanghae99.finalproject.model.dto.requestDto.*;
import com.hanghae99.finalproject.util.*;
import com.hanghae99.finalproject.util.resultType.*;
import lombok.*;

import javax.persistence.*;

import java.util.Optional;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Board extends TimeStamp {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = true)
    private String link;

    @Column(nullable = true)
    private String explanation;

    @Column(nullable = true)
    private String imgPath;

    @Column(nullable = true)
    private String content;

    @Column(nullable = false)
    private DisclosureStatus status;

    @Column(nullable = false)
    private BoardType boardType;

    @Column(nullable = false)
    private CategoryType category;

    @Column(nullable = false)
    private Long boardOrder;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "users_id")
    private Users users;

    @ManyToOne
    @JsonIgnore
    private Folder folder;

    @OneToOne
    @JsonIgnore
    private Share share;

    public Board(Long totalCont, BoardRequestDto boardRequestDto, Users user, Folder folder) {
        this.title = boardRequestDto.getTitle();
        this.link = boardRequestDto.getLink();
        this.explanation = boardRequestDto.getExplanation();
        this.imgPath = boardRequestDto.getImgPath();
        this.content = boardRequestDto.getContent();
        this.status = DisclosureStatus.PRIVATE;
        this.boardType = boardRequestDto.getBoardType();
        this.category = inNullCheck(boardRequestDto.getCategory());
        this.boardOrder = totalCont + 1;
        this.users = user;
        this.folder = folder;
    }

    public Board(BoardRequestDto boardRequestDto, long boardOrder, Users users, Folder folder) {
        this.title = boardRequestDto.getTitle();
        this.link = boardRequestDto.getLink();
        this.explanation = boardRequestDto.getExplanation();
        this.imgPath = boardRequestDto.getImgPath();
        this.content = boardRequestDto.getContent();
        this.status = DisclosureStatus.PRIVATE;
        this.boardType = boardRequestDto.getBoardType();
        this.category = inNullCheck(boardRequestDto.getCategory());
        this.boardOrder = boardOrder;
        this.users = users;
        this.folder = folder;
    }

    private CategoryType inNullCheck(CategoryType category) {
        if (!Optional.ofNullable(category).isPresent()) {
            return CategoryType.NO_CATEGORY;
        }
        return category;
    }

    public Board(BoardRequestDto boardRequestDto, Users user,Folder folder) {
        this.title = boardRequestDto.getTitle();
        this.explanation = boardRequestDto.getExplanation();
        this.imgPath = boardRequestDto.getImgPath();
        this.content = boardRequestDto.getContent();
        this.status = boardRequestDto.getStatus();
        this.boardType = boardRequestDto.getBoardType();
        this.category = boardRequestDto.getCategory();
        this.boardOrder = folder.getBoardCnt() + 1;
        this.users = user;
        this.folder = folder;
    }

    public Board(Board board, Users users, Folder folder) {
        this.title = board.getTitle();
        this.link = board.getLink();
        this.explanation = board.getExplanation();
        this.imgPath = board.getImgPath();
        this.content = board.getContent();
        this.status = board.getStatus();
        this.boardType = board.getBoardType();
        this.category = board.getCategory();
        this.boardOrder = folder.getBoardCnt()+1;
        this.users = users;
        this.folder = folder;
    }

    public void update(BoardRequestDto boardRequestDto) {
        this.title = boardRequestDto.getTitle();
        this.link = boardRequestDto.getLink();
        this.explanation = boardRequestDto.getExplanation();
        this.imgPath = boardRequestDto.getImgPath();
        this.content = boardRequestDto.getContent();
        this.status = boardRequestDto.getStatus();
        this.boardType = boardRequestDto.getBoardType();
        this.category = inNullCheck(boardRequestDto.getCategory());
    }

    public void updateOrder(Long order) {
        this.boardOrder = order;
    }

    public void addFolderId(Folder folder) {
        this.folder = folder;
    }

    public void updateStatus(FolderRequestDto folderRequestDto) {
        this.status = folderRequestDto.getStatus();
    }

}
