package com.main10.domain.review.entity;

import com.main10.domain.review.dto.ReviewDto;
import com.main10.global.util.Auditable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review {

    @Id
    @Column(name = "REVIEW_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double score;

    @Column(length = 1000)
    private String comment;

    private Long memberId;

    private Long placeId;
    
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;


    @Builder
    public Review(Long id,
                  Double score,
                  String comment,
                  Long memberId,
                  Long placeId,
                  LocalDateTime createdAt) {
        this.id = id;
        this.score = score;
        this.comment = comment;
        this.memberId = memberId;
        this.placeId = placeId;
        this.createdAt = createdAt;
    }

    public void editReview(Double score, String comment) {
        this.score = score;
        this.comment = comment;
    }
}
