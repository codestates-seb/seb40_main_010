package com.main21.place.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    @Column(length = 10000)
    private String detailInfo;

    private int maxCapacity;

    @Column(length = 500)
    private String address;

    private int charge;

    private double score;

    private int view;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    @OneToMany(mappedBy = "place")
    private List<PlaceCategory> placeCategories = new ArrayList<>();

    @OneToMany(mappedBy = "place")
    private List<PlaceImage> placeImages = new ArrayList<>();

    @OneToMany(mappedBy = "place")
    private List<MBTICount> mbtiCounts = new ArrayList<>();
}
