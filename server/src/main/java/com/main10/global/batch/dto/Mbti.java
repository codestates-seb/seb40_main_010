package com.main10.global.batch.dto;

import com.main10.domain.place.entity.Place;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class Mbti {

    @Getter
    @NoArgsConstructor
    public static class Response {
        private Long placeId;
        private String title;
        private Integer charge;
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
