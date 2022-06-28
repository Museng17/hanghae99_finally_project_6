package com.hanghae99.finalproject.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Users {

    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @Column (nullable = false, unique = true)
    private String username;

    @Column (nullable = false, unique = true)
    private String nickname;

    @Column (nullable = true)
    private String imgPath;

    @JsonIgnore
    @Column (nullable = false)
    private String password;

    public Users(String username, String nickname, String password) {
        this.username = username;
        this.nickname = nickname;
        this.password = password;
    }
}
