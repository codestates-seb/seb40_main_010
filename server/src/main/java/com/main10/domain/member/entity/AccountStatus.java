package com.main10.domain.member.entity;

import com.main10.global.exception.ExceptionCode;
import com.main10.global.security.exception.AuthException;

import static com.main10.global.security.utils.AuthConstants.*;

public enum AccountStatus {
    COMMON_MEMBER, KAKAO_MEMBER, GOOGLE_MEMBER, NAVER_MEMBER;

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
