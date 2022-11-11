package com.main21.place.dto;

import com.main21.place.entity.Category;
import com.main21.place.entity.PlaceImage;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import java.util.List;

public class PlaceDto {

    @Getter
    public static class Post {
        private String title;
        private List<Category> categories;
        private int maxCapacity;
        private String address;
        private String detailInfo;
        private List<PlaceImage> placeImages;
        private int charge;
    }

}
