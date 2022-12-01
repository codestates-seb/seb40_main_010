package com.main10.domain.member.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpHeaders;


public class TokenDto {

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
        private final AuthDto.Response response;
        private final HttpHeaders headers;

        @Builder
        public Response(AuthDto.Response response, HttpHeaders headers) {
            this.response = response;
            this.headers = headers;
        }
    }
}
