package com.hanghae99.finalproject.model.entity;

import com.hanghae99.finalproject.util.DisclosureStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Folder {

    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column (nullable = false)
    private String name;

    @Column (nullable = false)
    private DisclosureStatus status;

    @ManyToOne
    private Users users;

    @OneToMany
    @JoinColumn(name = "boardId")
    private List<Board> board = new ArrayList<>();

    @OneToOne
    private Share share;


    public Folder(Optional<Board> byId, Users user) {
        this.name = "test";
        this.status = byId.get().getStatus();
        this.users = user;
        this.board.add(byId.get());
    }
}
