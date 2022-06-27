package com.hanghae99.finalproject.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Users {

    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column (nullable = false, unique = true)
    private String email;

    @Column (nullable = false, unique = true)
    private String nickname;

    @Column (nullable = true)
    private String imgPath;

    @JsonIgnore
    @Column (nullable = false)
    private String password;

    @OneToMany
    @JoinColumn(name = "shareId")
    private List<Share> shares;

    @OneToMany
    @JoinColumn(name = "boardId")
    private List<Board> boards;

    @OneToMany
    @JoinColumn(name = "folderId")
    private List<Folder> folders;
}
