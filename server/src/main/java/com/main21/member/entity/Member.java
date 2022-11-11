package com.main21.member.entity;

import com.main21.util.Auditable;
import lombok.*;
import net.bytebuddy.dynamic.loading.InjectionClassLoader;
import org.springframework.data.annotation.Id;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Long Id;

    private String email;

    private String name;

    private String nickname;

    @Column(length = 500)
    private String password;

    private String phoneNumber;

    private MemberStatus memberStatus;

    private List<String> roles;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private MBTI mbti;
    @Builder
    public Member(String email, String name, String nickname, String password, String phoneNumber, List<String> roles) {
        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.memberStatus = MemberStatus.MEMBER_ACTIVE;
        this.roles = roles;
    }
    public enum MemberStatus {
        MEMBER_ACTIVE("활동중인 회원입니다."),
        MEMBER_SLEEP("휴먼 회원입니다."),
        MEMBER_EXITED("탈퇴한 회원입니다.");
        @Getter
        private final String status;

        MemberStatus(String status) {
            this.status = status;
        }
    }
}
