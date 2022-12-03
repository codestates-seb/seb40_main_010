package com.main10.domain.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;


public class TokenDto {

    @Getter
    @NoArgsConstructor
    public static class Token {
        private String accessToken;
        private String refreshToken;

        @Builder
        public Token(String accessToken, String refreshToken) {
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class Response {
        private AuthDto response;
        private HttpHeaders headers;

        @Builder
        public Response(AuthDto response, HttpHeaders headers) {
            this.response = response;
            this.headers = headers;
        }
    }
}
