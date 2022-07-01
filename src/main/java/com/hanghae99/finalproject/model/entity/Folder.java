package com.hanghae99.finalproject.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hanghae99.finalproject.model.dto.FolderRequestDto;
import com.hanghae99.finalproject.util.DisclosureStatus;
import lombok.*;

import javax.persistence.*;

import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Folder {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private DisclosureStatus status;

    @JsonIgnore
    @ManyToOne
    private Users users;

    @OneToOne
    private Share share;

    @OneToMany
    private List<Board> boardList = new ArrayList<>();

    public Folder(Long id, String name, DisclosureStatus status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

    public Folder(FolderRequestDto folderRequestDto, Users users) {
        this.name = folderRequestDto.getName();
        this.status = folderRequestDto.getStatus();
        this.users = users;
    }

    public void boardInFolder(List<Board> boards) {
        boardList = boards;
    }

    public void update(FolderRequestDto folderRequestDto) {
        this.name = folderRequestDto.getName();
        this.status = folderRequestDto.getStatus();
    }
}
