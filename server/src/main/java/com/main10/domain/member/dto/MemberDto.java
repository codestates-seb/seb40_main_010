package com.main10.domain.member.dto;

import lombok.Getter;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public class MemberDto {

    @Getter
    @NoArgsConstructor
    public static class Post {
        @NotEmpty(message = "mbti 정보는 공백이 아니어야 합니다.")
        private String mbti;

        @NotBlank(message = "이메일은 공백이 아니어야 합니다.")
        @Pattern(regexp = "/\\S+@\\S+\\.\\S+/"
                , message = "이메일 형식이 맞지 않습니다.")
        private String email;

        @NotBlank(message = "비밀번호는 공백이 아니어야 합니다.")
        @Pattern(regexp = "/^(?=.*[A-Za-z])(?=.*\\\\d)(?=.*[$@$!%*#?&])[A-Za-z\\\\d$@$!%*#?&]{8,}$/"
                , message = "8자 이상, 영문, 숫자, 특수문자가 포함되어야 합니다.")
        private String password;

        @NotBlank(message = "닉네임은 공백이 아니어야 합니다.")
        @Pattern(regexp = "/[0-9]|[a-z]|[A-Z]|[가-힣]/"
                , message = "한 글자 이상, 한글, 영문, 숫자만 쓸 수 있습니다.")
        private String nickname;

        @NotBlank(message = "전화번호는 공백이 아니어야 합니다.")
        @Pattern(regexp = "/^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$/"
                , message = "전화번호 양식에 맞지 않습니다.")
        private String phoneNumber;

        @Builder
        public Post(String mbti, String email, String password, String nickname, String phoneNumber) {
            this.mbti = mbti;
            this.email = email;
            this.password = password;
            this.nickname = nickname;
            this.phoneNumber = phoneNumber;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class Patch {
        @NotEmpty(message = "mbti 정보는 공백이 아니어야 합니다.")
        private String mbti;

        @NotBlank(message = "닉네임은 공백이 아니어야 합니다.")
        @Pattern(regexp = "/[0-9]|[a-z]|[A-Z]|[가-힣]/"
                , message = "한 글자 이상, 한글, 영문, 숫자만 쓸 수 있습니다.")
        private String nickname;

        @Builder
        public Patch(String mbti, String nickname) {
            this.mbti = mbti;
            this.nickname = nickname;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class Info {
        private String mbti;
        private String nickname;
        private String profileImage;

        @Builder
        public Info(String mbti, String nickname, String profileImage) {
            this.mbti = mbti;
            this.nickname = nickname;
            this.profileImage = profileImage;
        }
    }

}
