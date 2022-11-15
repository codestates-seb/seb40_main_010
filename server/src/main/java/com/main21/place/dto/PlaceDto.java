package com.main21.place.dto;

import com.main21.place.entity.Place;
import com.main21.place.entity.PlaceCategory;
import com.main21.place.entity.PlaceImage;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class PlaceDto {

    @Getter
    public static class Post {
        private String title;
        private List<String> categoryList;
        private int maxCapacity;
        private String address;
        private String detailInfo;
//        private List<PlaceImage> placeImages;
        private int charge;
    }

    @Getter
    public static class Response {
        private Long placeId;
        private String title;
        private int charge;
        private String image;
        private Double score;
        private String address;

        @Builder
        @QueryProjection
        public Response(Place place) {
            this.placeId = place.getId();
            this.title = place.getTitle();
            this.charge = place.getCharge();
            this.image = place.getPlaceImages().get(0).getFilePath();
            this.score = place.getScore();
            this.address = place.getAddress();

        }
    }

}
