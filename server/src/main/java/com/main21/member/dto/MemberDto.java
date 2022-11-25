package com.main21.member.dto;

import lombok.Getter;
import lombok.Builder;
import lombok.AllArgsConstructor;

public class MemberDto {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Post {

        private String mbti;

        private String email;

        private String password;

        private String nickname;

        private String phoneNumber;

    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Patch {

        private String mbti;

        private String nickname;

    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Info {

        private String mbti;

        private String nickname;

        private String profileImage;

    }

}
