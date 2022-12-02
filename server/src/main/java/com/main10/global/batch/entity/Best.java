package com.main10.global.batch.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity(name = "BEST")
@NoArgsConstructor
public class Best {

    @Id
    @Column(name = "BEST_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "MBTI")
    private String mbti;

    @Column(name = "PLACE_ID")
    private Long placeId;

    @Column(name = "TOTAL_COUNT")
    private Integer totalCount;

    @Builder
    public Best(Long id,
                String mbti,
                Long placeId,
                Integer totalCount) {
        this.id = id;
        this.mbti = mbti;
        this.placeId = placeId;
        this.totalCount = totalCount;
    }
}
