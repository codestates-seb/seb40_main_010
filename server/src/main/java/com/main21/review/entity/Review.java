package com.main21.review.entity;


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
public class Review extends Auditable {

    @Id
    @Column(name = "REVIEW_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double score;

    @Column(length = 1000)
    private String comment;


    private Long memberId;

    @Builder
    public Review(double score, String comment) {
        this.score = score;
        this.comment = comment;
    }
}
