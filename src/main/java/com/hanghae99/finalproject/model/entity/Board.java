package com.hanghae99.finalproject.model.entity;

import com.hanghae99.finalproject.util.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
}
