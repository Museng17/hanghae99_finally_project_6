package com.hanghae99.finalproject.model.resultType;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@NoArgsConstructor
public enum LoginType {
    @JsonProperty("user")
    USER("user"),

    @JsonProperty("google")
    GOOGLE("google"),

    @JsonProperty("Kakao")
    KAKAO("kakao");

    private String loginType;

    LoginType(String loginType) {
        this.loginType = loginType;
    }
}
