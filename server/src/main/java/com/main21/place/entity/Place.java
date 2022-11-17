package com.main21.place.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Place {

    // 공간 생성자
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PLACE_ID")
    private Long id;

    // 공간명
    private String title;

    // 공간 상세정보
    @Column(length = 10000)
    private String detailInfo;

    // 최대 수용 인원
    private int maxCapacity;

    // 공간 주소
    @Column(length = 500)
    private String address;

    // 시간별 금액
    private int charge;

    // 평점
    private double score;

    // 조회수
    private int view;


    // 공간 - 회원 간접 참
    private Long memberId;

    // 공간 - 공간 카테고리 1:N
    @OneToMany(mappedBy = "place")
    private List<PlaceCategory> placeCategories = new ArrayList<>();

    // 공간 - 공간 이미지 1:N
    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL)
    private List<PlaceImage> placeImages = new ArrayList<>();


    // 공간 - MBTI Count 1:N
//    @OneToMany(mappedBy = "place")
//    private List<MBTICount> mbtiCounts = new ArrayList<>();

    // createPlace 생성자
    @Builder
    public Place(String title, String detailInfo, int maxCapacity, String address, int charge, Long memberId, double score, int view){
        this.title = title;
        this.detailInfo = detailInfo;
        this.maxCapacity = maxCapacity;
        this.address = address;
        this.charge = charge;
        this.memberId = memberId;
        this.score = score;
        this.view = view;
    }

    public void addPlaceCategory(PlaceCategory placeCategory) {
        this.addPlaceCategory(placeCategory);
        if (placeCategory.getPlace() != this) {
            placeCategory.setPlace(this);
        }
    }


    public void addPlaceImage(PlaceImage placeImage) {
        this.placeImages.add(placeImage);

        if(placeImage.getPlace() != this) {
            placeImage.setPlace(this);
        }
    }
}
