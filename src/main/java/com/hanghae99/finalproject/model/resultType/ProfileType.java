package com.hanghae99.finalproject.model.resultType;

import lombok.*;

import java.util.*;

@Getter
@NoArgsConstructor
public enum ProfileType {
    BLACK("https://i.postimg.cc/2jXgpBV5/profile1.png"),
    YELLOW("https://i.postimg.cc/TYPL11jF/profile2.png"),
    ORANGE("https://i.postimg.cc/0N7rFwxM/profile3.png"),
    PURPLE("https://i.postimg.cc/XqYJr15d/profile4.png"),
    GREY("https://i.postimg.cc/154XPTsZ/profile5.png"),
    BLUE("https://i.postimg.cc/d0cV2Vdq/profile6.png");

    private String url;

    public static List<ProfileType> profileTypes = Arrays.asList(
            BLACK,
            YELLOW,
            ORANGE,
            PURPLE,
            PURPLE,
            GREY,
            BLUE
    );

    ProfileType(String url) {
        this.url = url;
    }

}
