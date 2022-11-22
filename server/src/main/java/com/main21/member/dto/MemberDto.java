package com.main21.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class MemberDto {

    @Getter
    @Builder
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

    @Getter
    public static class Info {
        private String profileImage;

        private String nickname;

        private String mbti;


        @Builder
        public Info(String profileImage, String nickname, String mbti) {
            this.profileImage = profileImage;
            this.nickname = nickname;
            this.mbti = mbti;
        }
    }

}
