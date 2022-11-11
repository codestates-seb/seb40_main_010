package com.main21.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 로그인 및 토큰 재발급을 위한 Dto 클래스 모음
 * @author mozzi327
 */
public class AuthDto {
    @Getter
    @AllArgsConstructor
    public static class Login {
        private String email;
        private String password;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Token {
        private String accessToken;
        private String refreshToken;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Response {
        private String accessToken;
        private String nickname;
        private String email;
    }
}
