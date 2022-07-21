package com.hanghae99.finalproject.model.dto.responseDto;

import com.hanghae99.finalproject.model.entity.Image;
import com.hanghae99.finalproject.model.resultType.ImageType;
import lombok.*;

@Getter
@NoArgsConstructor
public class ImageRequestDto {
    private Long id;
    private String imgPath;
    private ImageType imageType;

    public ImageRequestDto(Image image) {
        this.id = image.getId();
        this.imgPath = image.getImgPath();
        this.imageType = image.getImageType();
    }
}
