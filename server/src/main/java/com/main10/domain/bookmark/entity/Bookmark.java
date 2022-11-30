package com.main10.domain.bookmark.entity;


import com.main10.global.util.Auditable;
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
    public Bookmark(Long memberId, Long placeId, Long id) {
        this.id = id;
        this.bookmarkUrl = "http://localhost:3000/place/" + placeId;
        this.memberId = memberId;
        this.placeId = placeId;
    }
}
