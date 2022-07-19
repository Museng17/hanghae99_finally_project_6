package com.hanghae99.finalproject.exceptionHandler.CustumException;

import lombok.*;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    //엑세스 토큰
    NOT_HEADER_ACCESS_TOKEN(402, "헤더에 엑세스 토큰이 없습니다."),
    EXPIRATION_ACCESS_TOKEN(410, "기간이 만료된 엑세스 토큰입니다."),
    NOT_ACCESS_TOKEN(411, "변조되었거나 엑세스토큰이 아닙니다."),

    //리프레쉬토큰
    NOT_HEADER_REFRESH_TOKEN(502, "헤더에 리프레쉬 토큰이 없습니다."),
    EXPIRATION_REFRESH_TOKEN(510, "기간이 만료된 리프레쉬 토큰입니다."),
    NOT_REFRESH_TOKEN(511, "변조되어거나 리프레쉬 토큰이 아닙니다.");

    private final int statusCode;
    private final String massage;
}
