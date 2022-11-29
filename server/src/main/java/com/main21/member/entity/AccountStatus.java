package com.main21.member.entity;

import com.main21.exception.ExceptionCode;
import com.main21.security.exception.AuthException;

import static com.main21.security.utils.AuthConstants.*;

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
