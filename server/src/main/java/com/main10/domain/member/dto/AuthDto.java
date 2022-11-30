package com.main10.domain.member.dto;

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
    public static class Token {
        private final String accessToken;
        private final String refreshToken;

        @Builder
        public Token(String accessToken, String refreshToken) {
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
        }
    }

    @Getter
    public static class Response {
        private final String nickname;
        private final String email;


        @Builder
        public Response(String nickname, String email) {
            this.nickname = nickname;
            this.email = email;
        }
    }
}
