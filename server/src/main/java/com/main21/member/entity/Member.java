package com.main21.member.entity;

import com.main21.util.Auditable;
import lombok.*;

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
    private Long id;

    private String email;

    private String nickname;

    private String mbti;

    @Column(length = 500)
    private String password;

    private String phoneNumber;

    @Enumerated(value = EnumType.STRING)
    private MemberStatus memberStatus;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private MemberImage memberImage;

    @Builder
    public Member(String email,
                  String nickname,
                  String mbti,
                  String password,
                  String phoneNumber,
                  List<String> roles
                  ) {
        this.email = email;
        this.nickname = nickname;
        this.mbti = mbti;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.memberStatus = MemberStatus.MEMBER_ACTIVE;
        this.roles = roles;
    }

    public void setDetailsId(Long id) {
        this.id = id;
    }

    public void setDetailsEamil(String email) {
        this.email = email;
    }

    public void setDetailsPassword(String password) {
        this.password = password;
    }

    public void setDetailsRoles(List<String> roles) {
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

    public void editMember(String nickname,
                           String mbti) {
        this.nickname = nickname;
        this.mbti = mbti;
    }

    public void addMemberImage(MemberImage memberImage) {
        if (memberImage.getMember() != this) {
            memberImage.setMember(this);
        }
        this.memberImage = memberImage;
    }
}
