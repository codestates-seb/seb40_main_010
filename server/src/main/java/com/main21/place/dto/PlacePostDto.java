package com.main21.place.dto;

import com.main21.place.entity.PlaceImage;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class PlacePostDto {
    private Long memberId;
    private Long reserveId;
    private double score;
    private int view;
    private String title;
    private List<String> categoryList;
    private int maxCapacity;
    private String address;
    private String detailInfo;
    private int charge;
}
