package com.hanghae99.finalproject.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hanghae99.finalproject.model.dto.requestDto.*;
import com.hanghae99.finalproject.util.*;
import com.hanghae99.finalproject.util.resultType.*;
import lombok.*;

import javax.persistence.*;

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
    private String explanation;

    @Column(nullable = true)
    private String imgPath;

    @Column(nullable = true)
    private String content;

    @Column(nullable = false)
    private DisclosureStatus status;

    @Column(nullable = false)
    private BoardType boardType;

    @Column(nullable = true)
    private CategoryType category;

    @Column(nullable = false)
    private Long order;

    @ManyToOne
    @JsonIgnore
    private Users users;

    @ManyToOne
    @JsonIgnore
    private Folder folder;

    @OneToOne
    @JsonIgnore
    private Share share;

    public Board(Long totalCont, BoardRequestDto boardRequestDto, Users user) {
        this.title = boardRequestDto.getTitle();
        this.explanation = boardRequestDto.getExplanation();
        this.imgPath = boardRequestDto.getImgPath();
        this.content = boardRequestDto.getContent();
        this.status = boardRequestDto.getStatus();
        this.boardType = boardRequestDto.getBoardType();
        this.category = boardRequestDto.getCategory();
        this.order = totalCont + 1;
        this.users = user;
    }
    public Board(BoardRequestDto boardRequestDto, Users user) {
        this.title = boardRequestDto.getTitle();
        this.explanation = boardRequestDto.getExplanation();
        this.imgPath = boardRequestDto.getImgPath();
        this.content = boardRequestDto.getContent();
        this.status = boardRequestDto.getStatus();
        this.boardType = boardRequestDto.getBoardType();
        this.category = boardRequestDto.getCategory();
        this.users = user;
    }

    public void update(BoardRequestDto boardRequestDto) {
        this.title = boardRequestDto.getTitle();
        this.explanation = boardRequestDto.getExplanation();
        this.imgPath = boardRequestDto.getImgPath();
        this.content = boardRequestDto.getContent();
        this.status = boardRequestDto.getStatus();
        this.boardType = boardRequestDto.getBoardType();
        this.category = boardRequestDto.getCategory();
    }

    public void updateOrder(Long order) {
        this.order = order;
    }

    public void addFolderId(Folder folder) {
        this.folder = folder;
    }

    public void removeFolderId() {
        this.folder = null;
    }

    public void updateStatus(FolderRequestDto folderRequestDto) {
        this.status = folderRequestDto.getStatus();
    }

}
