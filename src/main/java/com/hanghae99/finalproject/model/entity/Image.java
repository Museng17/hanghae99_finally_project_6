package com.hanghae99.finalproject.model.entity;

import com.hanghae99.finalproject.model.resultType.ImageType;
import lombok.*;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Image {

    @Id
    private Long id;

    @Column(nullable = false)
    private String imgPath;

    @Column(nullable = false)
    private ImageType imageType;

    @ManyToOne
    private Board board;
}
