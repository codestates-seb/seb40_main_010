package com.main21.place.dto;

import com.main21.place.entity.Place;
import lombok.Getter;

import java.util.List;

@Getter
public class PlaceResponseDto {
    private Long placeId;
    private String title;
    private List<PlaceCategoryDto.Search> categoryList;
    private int maxCapacity;
    private String detailInfo;
    private int charge;
    private List<String> filePath;

    public PlaceResponseDto(Place place, List<String> filePath, List<PlaceCategoryDto.Search> categoryList) {
        this.placeId = place.getId();
        this.title = place.getTitle();
        this.categoryList = categoryList;
        this.maxCapacity = place.getMaxCapacity();
        this.detailInfo = place.getDetailInfo();
        this.charge = place.getCharge();
        this.filePath = filePath;
    }
}
