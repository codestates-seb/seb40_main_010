package com.main10.domain.member.entity;

import com.main10.global.util.Auditable;
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
    private MemberStatus memberStatus = MemberStatus.MEMBER_ACTIVE;

    @Enumerated(value = EnumType.STRING)
    private AccountStatus accountStatus = AccountStatus.COMMON_MEMBER;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private MemberImage memberImage;

    @Builder
    public Member(Long id,
                  String email,
                  String nickname,
                  String mbti,
                  String password,
                  AccountStatus accountStatus,
                  String phoneNumber,
                  List<String> roles) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.mbti = mbti;
        this.password = password;
        this.accountStatus = accountStatus;
        this.phoneNumber = phoneNumber;
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

    public void editMember(String nickname,
                           String mbti) {
        this.nickname = nickname;
        this.mbti = mbti;
    }

    public void setMemberStatus(MemberStatus memberStatus) {
        this.memberStatus = memberStatus;
    }

    public void addMemberImage(MemberImage memberImage) {
        if (memberImage.getMember() != this) {
            memberImage.setMember(this);
        }
        this.memberImage = memberImage;
    }
}
