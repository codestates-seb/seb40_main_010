package com.main21.bookmark.member.entity;

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
    private String userEmail;
    private String refreshToken;
    private Long userId;

    @Builder
    public Token(String userEmail,
                 String refreshToken,
                 Long userId) {
        this.userEmail = userEmail;
        this.refreshToken = refreshToken;
        this.userId = userId;
    }
}