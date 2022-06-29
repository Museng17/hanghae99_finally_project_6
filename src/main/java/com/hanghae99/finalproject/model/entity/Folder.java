package com.hanghae99.finalproject.model.entity;

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

    @ManyToOne
    private Users users;

    @OneToOne
    private Share share;

    @OneToMany
    @JoinColumn(name = "boardId")
    private List<Board> boardList = new ArrayList<>();

    public Folder(String test, Users users) {
        this.name = test;
        this.status = DisclosureStatus.PUBLIC;
        this.users = users;
    }

    public Folder(Long id, String name, DisclosureStatus status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }
}
