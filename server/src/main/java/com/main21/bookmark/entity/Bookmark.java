package com.main21.bookmark.entity;


import com.main21.member.entity.Member;
import com.main21.util.Auditable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Bookmark extends Auditable {

    @Id
    @Column(name = "BOOKMARK_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1000)
    private String bookmarkUrl;

    private Long memberId;

    private Long placeId;

    @Builder
    public Bookmark(Long memberId, Long placeId) {
        this.bookmarkUrl = "http://localhost:3000/" + placeId;
        this.memberId = memberId;
        this.placeId = placeId;
    }
}