package com.hanghae99.finalproject.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import java.util.List;

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

    @ManyToOne
    private Users users;

    @OneToMany
    @JoinColumn(name = "boardId")
    private List<Board> board;

    @OneToOne
    private Share share;
}
