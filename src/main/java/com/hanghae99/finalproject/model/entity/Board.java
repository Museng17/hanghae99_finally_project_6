package com.hanghae99.finalproject.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hanghae99.finalproject.model.dto.BoardRequestDto;
import com.hanghae99.finalproject.util.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.catalina.User;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Board {

    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column (nullable = false)
    private String title;

    @Column (nullable = true)
    private String explanation;

    @Column (nullable = true)
    private String imgPath;

    @Column (nullable = true)
    private String content;

    @Column (nullable = false)
    private DisclosureStatus status;

    @Column (nullable = false)
    private BoardType boardType;

    @ManyToOne
    @JsonIgnore
    private Users users;

    @ManyToOne
    @JsonIgnore
    private Folder folder;

    @OneToOne
    @JsonIgnore
    private Share share;

    public Board(BoardRequestDto boardRequestDto, Users user) {
        this.title = boardRequestDto.getTitle();
        this.explanation = boardRequestDto.getExplanation();
        this.imgPath = boardRequestDto.getImgPath();
        this.content = boardRequestDto.getContent();
        this.status = boardRequestDto.getStatus();
        this.boardType = boardRequestDto.getBoardType();
        this.users = user;  //테스트 유저
    }

    public void update(BoardRequestDto boardRequestDto) {
        this.title = boardRequestDto.getTitle();
        this.explanation = boardRequestDto.getExplanation();
        this.imgPath = boardRequestDto.getImgPath();
        this.content = boardRequestDto.getContent();
        this.status = boardRequestDto.getStatus();
        this.boardType = boardRequestDto.getBoardType();
    }

    public void addFolderId(Folder test) {
        this.folder = test;
    }
}
