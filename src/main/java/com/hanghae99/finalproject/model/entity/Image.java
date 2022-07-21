package com.hanghae99.finalproject.model.entity;

import com.hanghae99.finalproject.model.resultType.ImageType;
import lombok.*;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String imgPath;

    @Column(nullable = false)
    private ImageType imageType;

    @ManyToOne
    private Board board;

    public Image(Board board, ImageType imageType) {
        this.imageType = imageType;
        this.imgPath = board.getImgPath();
        this.board = board;
    }
}
