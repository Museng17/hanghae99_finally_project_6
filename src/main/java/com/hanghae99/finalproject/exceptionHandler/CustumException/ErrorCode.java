package com.hanghae99.finalproject.exceptionHandler.CustumException;

import lombok.*;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    NOT_FIND_USER(500, 200, "유저를 찾을 수 없습니다."),
    NOT_FIND_FOLDER(500, 200, "모음를 찾을 수 없습니다"),
    NOT_FIND_BOARD(500, 200, "조각을 찾을 수 없습니다"),
    NOT_FIND_CHOICE_IMAGE(501, 200, "선택된 이미지를 찾을 수 없습니다."),
    OVERLAP_EMAIL(501, 200, "중복된 이메일 입니다."),
    NOT_EMAIL_CERTIFICATION_CHECK(404, 200, "이메일 인증을 하지 않은 회원입니다."),
    NOT_FIND_SHARE(500,200,"공유된 폴더를 찾을 수 없습니다."),
    EXIST_REPORT(500,200,"이미 신고하셨습니다."),
    NOT_EMAIL(505, 200, "이메일를 형식이 다릅니다."),
    NOT_CERTIFICATION(505, 200, "인증번호를 입력해주세요"),

    //엑세스 토큰
    NOT_HEADER_ACCESS_TOKEN(402, 402, "헤더에 엑세스 토큰이 없습니다."),
    EXPIRATION_ACCESS_TOKEN(410, 410, "기간이 만료된 엑세스 토큰입니다."),
    NOT_ACCESS_TOKEN(411, 411, "변조되었거나 엑세스토큰이 아닙니다."),

    //리프레쉬토큰
    NOT_HEADER_REFRESH_TOKEN(502, 502, "헤더에 리프레쉬 토큰이 없습니다."),
    EXPIRATION_REFRESH_TOKEN(510, 510, "기간이 만료된 리프레쉬 토큰입니다."),
    NOT_REFRESH_TOKEN(511, 511, "변조되어거나 리프레쉬 토큰이 아닙니다.");

    private final int statusCode;
    private final int realStatusCode;
    private final String message;
}
