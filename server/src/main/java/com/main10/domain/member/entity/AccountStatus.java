package com.main10.domain.member.entity;

import com.main10.global.exception.ExceptionCode;
import com.main10.global.security.exception.AuthException;
import lombok.Getter;

import static com.main10.global.security.utils.AuthConstants.*;

public enum AccountStatus {
    COMMON_MEMBER("common"),
    KAKAO_MEMBER("kakao"),
    GOOGLE_MEMBER("google"),
    NAVER_MEMBER("naver");

    @Getter
    private final String provider;

    AccountStatus(String provider) {
        this.provider = provider;
    }

    public static AccountStatus whatIsProvider(String provider) {
        switch (provider) {
            case GOOGLE :
                return AccountStatus.GOOGLE_MEMBER;
            case KAKAO :
                return AccountStatus.KAKAO_MEMBER;
            case NAVER :
                return AccountStatus.NAVER_MEMBER;
            default:
                throw new AuthException(ExceptionCode.INVALID_OAUTH2);
        }
    }
}
