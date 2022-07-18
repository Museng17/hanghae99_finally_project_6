package com.hanghae99.finalproject.model.resultType;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.*;

@Getter
@NoArgsConstructor
public enum CategoryType {
    @JsonProperty("전체")
    ALL("전체"),

    @JsonProperty("디자인")
    DESIGN("디자인"),

    @JsonProperty("음악")
    MUSIC("음악"),

    @JsonProperty("음식")
    FOOD("음식"),

    @JsonProperty("건강")
    HEALTH("건강"),

    @JsonProperty("영화")
    MOVIE("영화"),

    @JsonProperty("스포츠")
    SPORTS("스포츠"),

    @JsonProperty("취미")
    HOBBY("취미"),

    @JsonProperty("여행")
    TRAVEL("여행"),

    @JsonProperty("공연")
    SHOW("공연"),

    @JsonProperty("전시회")
    EXHIBITION("전시회"),

    @JsonProperty("공부")
    STUDY("공부"),

    @JsonProperty("비즈니스")
    BUSINESS("비즈니스"),

    @JsonProperty("패션")
    FASHION("패션"),

    @JsonProperty("경제")
    ECONOMY("경제"),

    @JsonProperty("카페")
    CAFE("카페"),

    @JsonProperty("쇼핑")
    SHOPPING("쇼핑"),

    @JsonProperty("미정")
    NO_CATEGORY("미정"),

    @JsonProperty("기타")
    OTHERS("기타");

    private String name;

    public final static List<CategoryType> ALL_CATEGORYT = Arrays.asList(
            ALL,
            DESIGN,
            MUSIC,
            FOOD,
            HEALTH,
            MOVIE,
            SPORTS,
            HOBBY,
            TRAVEL,
            SHOW,
            EXHIBITION,
            STUDY,
            BUSINESS,
            FASHION,
            NO_CATEGORY,
            ECONOMY,
            CAFE,
            SHOPPING,
            OTHERS
    );

    CategoryType(String name) {
        this.name = name;
    }

}
