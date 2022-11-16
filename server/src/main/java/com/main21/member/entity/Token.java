package com.main21.member.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Token {

    @Id
    @GeneratedValue
    @Column(name = "REFRESH_TOKEN_ID")
    private Long id;
    private String memberEmail;
    private String refreshToken;
    private Long memberId;

    @Builder
    public Token(String memberEmail,
                 String refreshToken,
                 Long memberId) {
        this.memberEmail = memberEmail;
        this.refreshToken = refreshToken;
        this.memberId = memberId;
    }
}