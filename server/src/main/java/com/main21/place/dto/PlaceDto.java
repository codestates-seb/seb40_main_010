package com.main21.place.dto;

import com.main21.place.entity.Place;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

public class PlaceDto {

    @Getter
    public static class SearchDetail {

        private int startCharge;

        private int endCharge;

        private int capacity;
    }

    @Getter
    public static class Response{
        private Long placeId;

        private String title;

        private int charge;

        private String image;

        private Double score;

        private String address;

        private Integer endTime;

        @Builder
        @QueryProjection
        public Response(Place place) {
            this.placeId = place.getId();
            this.title = place.getTitle();
            this.score = place.getScore();
            this.charge = place.getCharge();
            this.address = place.getAddress();
            this.endTime = place.getEndTIme();
            this.image = place.getPlaceImages().get(0).getFilePath();
        }
    }

}
