package com.hanghae99.finalproject.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
public class OgResponseDto {
    String title;
    String image;
    String description;
    public OgResponseDto(String Title,String Image,String Description){
        this.title = Title;
        this.image = Image;
        this.description = Description;
    }
}
