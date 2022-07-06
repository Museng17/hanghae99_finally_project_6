package com.hanghae99.finalproject.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hanghae99.finalproject.model.dto.requestDto.FolderRequestDto;
import com.hanghae99.finalproject.util.*;
import com.hanghae99.finalproject.util.resultType.CategoryType;
import lombok.*;

import javax.persistence.*;

import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Folder extends TimeStamp {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private DisclosureStatus status;

    @Column(nullable = true)
    private CategoryType category;

    @Column(nullable = false)
    private Long order;

    @JsonIgnore
    @ManyToOne
    private Users users;

    @OneToOne
    private Share share;

    @OneToMany
    @JoinColumn(name = "folder_id")
    private List<Board> boardList = new ArrayList<>();

    public Folder(Long id, String name, DisclosureStatus status, CategoryType category) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.category = category;
    }

    public Folder(FolderRequestDto folderRequestDto, Users users, Long folderCount) {
        this.name = folderRequestDto.getName();
        this.status = folderRequestDto.getStatus();
        this.category = folderRequestDto.getCategory();
        this.order = folderCount + 1;
        this.users = users;
    }

    public void update(FolderRequestDto folderRequestDto) {
        this.name = folderRequestDto.getName();
        this.status = folderRequestDto.getStatus();
        this.category = folderRequestDto.getCategory();
    }

    public void updateOrder(Long order) {
        this.order = order;
    }
}
