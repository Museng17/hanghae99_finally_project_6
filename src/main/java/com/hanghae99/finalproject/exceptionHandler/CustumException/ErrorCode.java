package com.hanghae99.finalproject.exceptionHandler.CustumException;

import lombok.*;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    //믹스매치
    MIX_MATCH_USER(4040, 200, "글쓴이가 아닙니다."),
    MIX_MATCH_ORDER_NUM(4040, 200, "정확한 order number가 아닙니다."),

    //할 수 없습니다.
    CNT_NOT_DO_IT(5000, 200, "삭제할 수 없습니다."),

    //중복
    OVERLAP_EMAIL(501, 200, "중복된 이메일 입니다."),
    OVERLAP(501, 200, "중복되었습니다."),
    OVERLAP_NICKNAME(501, 200, "중복된 닉네임입니다."),
    EXIST_REPORT(500,200,"이미 신고하셨습니다."),

    //찾을 수 없습니다.
    NOT_FIND_CHOICE_IMAGE(501, 200, "선택된 이미지를 찾을 수 없습니다."),
    NOT_FIND_USER(404, 200, "유저를 찾을 수 없습니다."),
    NOT_FIND_FOLDER(500, 200, "모음를 찾을 수 없습니다"),
    NOT_FIND_BOARD(500, 200, "조각을 찾을 수 없습니다"),
    NOT_FIND_SHARE(500,200,"공유된 폴더를 찾을 수 없습니다."),
    NOT_FOUND_TARGET(500, 200, "대상을 찾을 수 없습니다."),

    //형식이 다르거나 유효성 검사
    NOT_EMAIL(505, 200, "이메일를 형식이 다릅니다."),
    OVER_TEXT(500, 200, "글자수를 확인해주세요"),
    CONTENT_OVER_TEXT(505, 200, "내용 글자수를 확인해주세요"),
    TITLE_OVER_TEXT(505, 200, "제목 글자수를 확인해주세요"),

    //사용할 수 없습니다.
    CNT_NOT_USE_NICKNAME(505, 200, "이용할 수 없는 닉네임입니다."),
    NOT_USERNAME(505, 200, "사용할 수 없는 아이디입니다."),
    NOT_USE_PASSWORD(505, 200, "사용할 수 없는 비밀번호입니다."),
    NOT_USE_EXT(505, 200, "사용할 수 없는 확장자 파일입니다."),
    NOT_USE_SUBJECT(505, 200, "사용할 수 없는 제목입니다."),

    //인증실패
    NOT_CERTIFICATION(505, 200, "인증번호를 입력해주세요"),
    NOT_EMAIL_CERTIFICATION_CHECK(404, 200, "이메일 인증을 하지 않은 회원입니다."),

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
