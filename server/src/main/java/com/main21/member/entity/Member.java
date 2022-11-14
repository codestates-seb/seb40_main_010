package com.main21.member.entity;

import com.main21.bookmark.entity.Bookmark;
import com.main21.place.entity.Place;
import com.main21.reserve.entity.Reserve;
import com.main21.review.entity.Review;
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

    private String name;

    private String nickname;

    @Column(length = 500)
    private String password;

    private String phoneNumber;

    private MemberStatus memberStatus;

    private String profileImage;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();

//    @OneToOne(mappedBy = "member", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
//    private MBTI mbti;
//
//    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
//    private List<Bookmark> bookmarks = new ArrayList<>();
//
//    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
//    private List<Review> reviews = new ArrayList<>();
//
//    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
//    private List<Reserve> reserves = new ArrayList<>();
//
//    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
//    private List<Place> places = new ArrayList<>();


    @Builder
    public Member(String email, String name, String nickname, String password, String phoneNumber, List<String> roles, String profileImage) {
        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.memberStatus = MemberStatus.MEMBER_ACTIVE;
        this.roles = roles;
        this.profileImage = profileImage;
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
