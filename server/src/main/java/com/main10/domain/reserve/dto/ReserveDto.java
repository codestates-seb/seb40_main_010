package com.main10.domain.reserve.dto;

import com.main10.domain.place.entity.Place;
import com.main10.domain.reserve.entity.Reserve;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

public class ReserveDto {

    @Getter
    @NoArgsConstructor
    public static class Post {

        @Positive(message = "인원 수가 0이 되어서는 안됩니다.")
        private int capacity;

        @NotNull(message = "체크인 시간은 공백이 아니어야 합니다.")
        private LocalDateTime startTime;

        @NotNull(message = "체크아웃 시간은 공백이 아니어야 합니다.")
        private LocalDateTime endTime;

        @Builder
        public Post(int capacity, LocalDateTime startTime, LocalDateTime endTime) {
            this.capacity = capacity;
            this.startTime = startTime;
            this.endTime = endTime;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class Patch {
        @NotNull(message = "인원 수는 공백이 아니어야 합니다.")
        @Positive(message = "인원 수가 0이 되어서는 안됩니다.")
        private int capacity;

        @NotNull(message = "체크인 시간은 공백이 아니어야 합니다.")
        private LocalDateTime startTime;

        @NotNull(message = "체크아웃 시간은 공백이 아니어야 합니다.")
        private LocalDateTime endTime;

        @Builder
        public Patch(int capacity, LocalDateTime startTime, LocalDateTime endTime) {
            this.capacity = capacity;
            this.startTime = startTime;
            this.endTime = endTime;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class Response {

        private Long reserveId;
        private Long placeId;
        private String title;
        private String address;
        private String image;
        private int capacity;
        private String status;
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
            this.status = reserve.getStatus().getStatus();
            this.capacity = reserve.getCapacity();
            this.startTime = reserve.getStartTime();
            this.endTime = reserve.getEndTime();
            this.totalCharge = reserve.getTotalCharge();
        }
    }


    @Getter
    @NoArgsConstructor
    public static class Detail {
        private LocalDateTime startTime;
        private LocalDateTime endTime;

        @Builder
        @QueryProjection
        public Detail(LocalDateTime startTime, LocalDateTime endTime) {
            this.startTime = startTime;
            this.endTime = endTime;
        }
    }
}
