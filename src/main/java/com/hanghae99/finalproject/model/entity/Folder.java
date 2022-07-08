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

    @Column(nullable = false)
    private Long sharedCount = 0L;




    @Column(nullable = false)
    private Long folderOrder;

    @JsonIgnore
    @ManyToOne
    private Users users;

    @OneToOne
    private Share share;

    @OneToMany
    @JoinColumn(name = "folder_id")
    private List<Board> boardList = new ArrayList<>();

    public Folder(Long id, String name, DisclosureStatus status) {
        this.id = id;
        this.name = name;
        this.status = status;

    }

    public Folder( Users users) {
        this.name = "무제";
        this.status = DisclosureStatus.PUBLIC ;
        this.folderOrder = 1L;
        this.users = users;
    }
    public Folder(FolderRequestDto folderRequestDto, Users users, Long folderCount) {
        this.name = folderRequestDto.getName();
        this.status = folderRequestDto.getStatus();
        this.folderOrder = folderCount + 1;
        this.users = users;
    }
    public Folder(FolderRequestDto folderRequestDto, Users users) {
        this.name = folderRequestDto.getName();
        this.status = folderRequestDto.getStatus();
        this.users = users;
        this.sharedCount = folderRequestDto.getSharedCount()+1;
    }

    public void update(FolderRequestDto folderRequestDto) {
        this.name = folderRequestDto.getName();
        this.status = folderRequestDto.getStatus();
    }

    public void updateOrder(Long order) {
        this.folderOrder = order;
    }
}
