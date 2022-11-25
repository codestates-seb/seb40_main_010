package com.main21.exception;

import lombok.Getter;

public enum ExceptionCode {
    PLACE_NOT_FOUND(504, "존재하지 않는 호스팅입니다."),
    MEMBER_NOT_FOUND(504, "존재하지 않는 회원입니다."),
    REVIEW_NOT_FOUND(504, "존재하지 않는 후기입니다."),
    BOOKMARK_NOT_FOUND(504, "존재하지 않는 북마크입니다."),
    CATEGORY_NOT_FOUND(504, "존재하지 않는 카테고리입니다."),
    RESERVATION_NOT_FOUND(504, "존재하지 않는 예약입니다."),

    INVALID_ACCESS(403, "유효하지 않은 접근입니다."),
    INVALID_MEMBER(403, "회원 정보를 다시 확인하세요."),

    //토큰
    INVALID_AUTH_TOKEN(504, "권한 정보가 없는 토큰입니다."),
    INVALID_REFRESH_TOKEN(504, "리프레시 토큰이 유효하지 않습니다."),
    MISMATCH_ACCESS_TOKEN(504, "엑세스 토큰의 유저 정보가 일치하지 않습니다."),
    REFRESH_TOKEN_NOT_FOUND(504, "로그아웃 된 사용자입니다."),
    MISSING_HEADER_ACCESS_TOKEN(504,"헤더에 엑세스 토큰을 넣어주세요."),


    // 중복여부
    EMAIL_ALREADY_EXIST(504, "이미 존재하는 이메일입니다."),
    NICKNAME_ALREADY_EXIST(504, "이미 존재하는 닉네임입니다."),
    RESOURCE_ALREADY_EXIST(504, "이미 존재하는 데이터입니다."),

    // 요청 실패
    UPLOAD_FAILED(504, "업로드가 실패했습니다."),
    PAYMENT_URL_REQUEST_FAILED(504, "결제 URL 요청을 실패했습니다."),

    NO_IMAGE(504, "이미지가 비어있습니다."),
    UNAUTHORIZED_FOR_UPDATE(403, "수정 권한이 없습니다."),
    RESERVATION_MAX_CAPACITY_OVER(504, "최대 수용 인원을 초과했습니다.");

    @Getter
    private final int status;

    @Getter
    private final String message;

    ExceptionCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
