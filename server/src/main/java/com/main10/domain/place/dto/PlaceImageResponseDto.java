package com.main10.domain.place.dto;

import com.main10.domain.place.entity.PlaceImage;
import lombok.Getter;

@Getter
public class PlaceImageResponseDto {
    private String filePath;

    public PlaceImageResponseDto(PlaceImage placeImage) {
        this.filePath = placeImage.getFilePath();
    }
}
