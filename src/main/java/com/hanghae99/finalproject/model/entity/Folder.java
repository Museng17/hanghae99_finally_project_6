package com.hanghae99.finalproject.model.entity;

import com.hanghae99.finalproject.model.dto.requestDto.FolderRequestDto;
import com.hanghae99.finalproject.model.resultType.*;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@NamedEntityGraph(name = "Folder.fetchUser", attributeNodes = @NamedAttributeNode("users"))
public class Folder extends TimeStamp {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private DisclosureStatusType status;

    @Column(nullable = false)
    private Long sharedCount = 0L;

    @Column(nullable = false)
    private Long folderOrder;

    @Column(nullable = false)
    private Long boardCnt;

    @Column(nullable = false)
    private Long reportCnt = 0L;

    @ManyToOne(fetch = FetchType.LAZY)
    private Users users;

    @OneToOne(fetch = FetchType.LAZY)
    private Share share;

    public Folder(Long id, String name, DisclosureStatusType status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

    public Folder(Users users) {
        this.name = "무제";
        this.status = DisclosureStatusType.PUBLIC;
        this.folderOrder = 1L;
        this.users = users;
        this.boardCnt = 0L;
    }

    public Folder(FolderRequestDto folderRequestDto, Users users, Long folderCount) {
        this.name = folderRequestDto.getName();
        this.status = folderRequestDto.getStatus();
        this.folderOrder = folderCount + 1;
        this.users = users;
        this.boardCnt = 0L;
    }

    public Folder(Long id, DisclosureStatusType status) {
        this.id = id;
        this.status = status;
    }

    public void update(FolderRequestDto folderRequestDto) {
        this.name = folderRequestDto.getName();
        this.status = folderRequestDto.getStatus();
    }

    public void updateOrder(Long order) {
        this.folderOrder = order;
    }

    public void updateBoardCnt(long boardCnt) {
        this.boardCnt = boardCnt;
    }

    public void updateStatus(DisclosureStatusType status) {
        this.status = status;
    }
}
