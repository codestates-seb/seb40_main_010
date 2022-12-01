package com.main10.domain.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 로그인 및 토큰 재발급을 위한 Dto 클래스 모음
 * @author mozzi327
 */
@Getter
@NoArgsConstructor
public class AuthDto {
    private String nickname;
    private String email;
    private String mbti;

    @Builder
    public AuthDto(String nickname, String email, String mbti) {
        this.nickname = nickname;
        this.email = email;
        this.mbti = mbti;
    }
}
