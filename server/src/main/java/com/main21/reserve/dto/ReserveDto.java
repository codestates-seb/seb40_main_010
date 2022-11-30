package com.main21.reserve.dto;

import com.main21.place.entity.Place;
import com.main21.reserve.entity.Reserve;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class ReserveDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Post {
        @NotBlank
        private int capacity;

        private LocalDateTime startTime;

        private LocalDateTime endTime;
    }
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Patch {
        @NotBlank
        private int capacity;

        private LocalDateTime startTime;

        private LocalDateTime endTime;
    }

    @Getter
    public static class Response implements Comparable<Response>{

        private Long reserveId;

        private Long placeId;
        private String title;
        private String address;
        private String image;
        private int capacity;

        private LocalDateTime startTime;

        private LocalDateTime endTime;

        private Long totalCharge;

        @Builder
        @QueryProjection
        public Response(Reserve reserve, Place place) {
            this.reserveId = reserve.getId();
            this.placeId = place.getId();
            this.title = place.getTitle();
            this.address = place.getAddress();
            this.image = place.getPlaceImages().get(0).getFilePath();
            this.capacity = reserve.getCapacity();
            this.startTime = reserve.getStartTime();
            this.endTime = reserve.getEndTime();
            this.totalCharge = reserve.getTotalCharge();
        }

        @Override
        public int compareTo(Response o) {
            return (int)(this.reserveId-o.reserveId);
        }
    }


    @Getter
    public static class Detail {
        private final LocalDateTime startTime;
        private final LocalDateTime endTime;

        @Builder
        @QueryProjection
        public Detail(LocalDateTime startTime, LocalDateTime endTime) {
            this.startTime = startTime;
            this.endTime = endTime;
        }
    }
}
