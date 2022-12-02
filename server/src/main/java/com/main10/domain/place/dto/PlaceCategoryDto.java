package com.main10.domain.place.dto;

import com.main10.domain.place.entity.Place;
import com.main10.domain.place.entity.PlaceImage;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

public class PlaceCategoryDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Search {
        private Long categoryId;
        private String categoryName;
    }

    @Getter
    public static class Response {
        private final Long categoryId;
        private final Long placeId;
        private final String title;
        private final int charge;
        private final String image;
        private final Double score;
        private final String address;

        @Builder
        @QueryProjection
        public Response(Place place, Long categoryId) {
            this.categoryId = categoryId;
            this.placeId = place.getId();
            this.title = place.getTitle();
            this.charge = place.getCharge();
            this.image = place.getPlaceImages().get(0).getFilePath();
            this.score = place.getScore();
            this.address = place.getAddress();
        }
    }

    @Getter
    public static class ResponseTest {
        private final Long categoryId;
        private final Long placeId;
        private final String title;
        private final int charge;
        private final String image;
        private final Double score;
        private final String address;

        @Builder
        @QueryProjection
        public ResponseTest(Place place, PlaceImage placeImage, Long categoryId) {
            this.categoryId =categoryId;
            this.placeId = place.getId();
            this.title = place.getTitle();
            this.charge = place.getCharge();
            this.image = placeImage.getFilePath();
            this.score = place.getScore();
            this.address = place.getAddress();
        }
    }
}
