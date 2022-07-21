package com.hanghae99.finalproject.model.entity;

import com.hanghae99.finalproject.model.dto.requestDto.BoardRequestDto;
import com.hanghae99.finalproject.model.dto.responseDto.ImageRequestDto;
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

    public Image(Board board, ImageRequestDto image) {
        this.imageType = image.getImageType();
        this.imgPath = image.getImgPath();
        this.board = board;
    }

    public void imagePathUpdate(BoardRequestDto boardRequestDto) {
        this.imgPath = boardRequestDto.getImgPath();
    }
}
