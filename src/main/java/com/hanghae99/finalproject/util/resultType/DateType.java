package com.hanghae99.finalproject.util.resultType;

import lombok.*;

@Getter
@NoArgsConstructor
public enum DateType {
    YEAR_MONTH_DAY("yyyy년 MM월 dd일");

    private String pattern;

    DateType(String pattern) {
        this.pattern = pattern;
    }
}
