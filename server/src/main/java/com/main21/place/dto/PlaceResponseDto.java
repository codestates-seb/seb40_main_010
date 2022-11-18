package com.main21.place.dto;

import com.main21.place.entity.Place;
import lombok.Getter;

import java.util.List;

@Getter
public class PlaceResponseDto {
    private Long placeId;
    private String title;
    private List<String> category;
    private int maxCapacity;
    private String detailInfo;
    private int charge;
    private double score;
    private List<String> filePath;


    public PlaceResponseDto(Place place, List<String> filePath, List<String> category) {
        this.placeId = place.getId();
        this.title = place.getTitle();
        this.category = category;
        this.maxCapacity = place.getMaxCapacity();
        this.detailInfo = place.getDetailInfo();
        this.charge = place.getCharge();
        this.score = place.getScore();
        this.filePath = filePath;
    }
}
