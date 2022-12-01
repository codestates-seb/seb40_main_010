package com.main10.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 로그인 및 토큰 재발급을 위한 Dto 클래스 모음
 * @author mozzi327
 */
public class AuthDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Login {
        private String email;
        private String password;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Token {
        private String accessToken;
        private String refreshToken;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private String nickname;
        private String email;
        private String mbti;
    }
}
