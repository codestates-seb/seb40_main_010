package com.main21.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class MemberDto {

    @Getter
    @AllArgsConstructor
    public static class Post {
        private String email;

        private String password;

        private String nickname;

        private String phoneNumber;

        private String mbti;

    }

    @Getter
    public static class Patch {
        private String nickname;
        private String mbti;
    }

}
