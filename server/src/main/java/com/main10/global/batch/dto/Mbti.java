package com.main10.global.batch.dto;

import com.main10.domain.place.entity.Place;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

public class Mbti {

    @Getter
    public static class Response {
        private final Long placeId;
        private final String title;
        private final Integer charge;
        private final String image;
        private final Double score;
        private final String address;

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
