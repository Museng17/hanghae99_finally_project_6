package com.hanghae99.finalproject.model.resultType;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@NoArgsConstructor
public enum ImageType {

    @JsonProperty("profile")
    PROFILE("profile"),          // 프로필사진타입

    @JsonProperty("basic")
    BASIC("basic"),             // 추천이미지

    @JsonProperty("og")
    OG("og"),                   // OG태그 이미지

    @JsonProperty("self")
    SELF("self");              // 자신이 추가한 이미지

    private String imageType;

    ImageType(String imageType) {
        this.imageType = imageType;
    }
}
