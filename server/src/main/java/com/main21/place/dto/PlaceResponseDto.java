package com.main21.place.dto;

import com.main21.place.entity.Place;
import lombok.Getter;

import java.util.List;

@Getter
public class PlaceResponseDto {
    private Long placeId;
    private String title;
    //private List<String> categoryList;
    private int maxCapacity;
    private String detailInfo;
    private int charge;
    //private List<Long> fileId;
    private List<String> filePath;

    public PlaceResponseDto(Place place, List<String> filePath) {//List<Long> fileId) {
        this.placeId = place.getId();
        this.title = place.getTitle();
        //this.categoryList = place.getPlaceCategories();
        this.maxCapacity = place.getMaxCapacity();
        this.detailInfo = place.getDetailInfo();
        this.charge = place.getCharge();
        //this.fileId = fileId;
        this.filePath = filePath;
    }
}
