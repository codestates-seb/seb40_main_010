package com.main21.place.dto;

import com.main21.place.entity.Place;
import com.main21.place.entity.PlaceImage;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PlaceImageResponseDto {
    private String filePath;

    public PlaceImageResponseDto(PlaceImage placeImage) {
        this.filePath = placeImage.getFilePath();
    }
}
