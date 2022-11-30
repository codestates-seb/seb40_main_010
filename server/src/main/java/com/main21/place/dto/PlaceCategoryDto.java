package com.main21.place.dto;

import com.main21.place.entity.Place;
import com.main21.place.entity.PlaceCategory;
import com.main21.place.entity.PlaceImage;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PlaceCategoryDto {

    @Getter
    @Builder
    public static class Search {
        private Long categoryId;
        private String categoryName;

/*        public Search(PlaceCategory placeCategory) {
            this.categoryId = placeCategory.getId();
            this.categoryName = placeCategory.getCategoryName();
        }*/
    }

    @Getter
    public static class Response {
        private Long categoryId;
        private Long placeId;
        private String title;
        private int charge;
        private String image;
        private Double score;
        private String address;

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
        private Long categoryId;
        private Long placeId;
        private String title;
        private int charge;
        private String image;
        private Double score;
        private String address;

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
