package com.main21.member.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MBTI {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MBTI_ID")
    private Long id;
    private String mbtiStatus;

    @OneToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    public MBTI(String mbtiStatus) {
        this.mbtiStatus = mbtiStatus;
    }
}