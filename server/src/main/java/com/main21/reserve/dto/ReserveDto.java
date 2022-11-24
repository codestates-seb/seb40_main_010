package com.main21.reserve.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.main21.bookmark.dto.BookmarkDto;
import com.main21.member.entity.Member;
import com.main21.place.entity.Place;
import com.main21.reserve.entity.Reserve;
import com.main21.review.dto.ReviewDto;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Date;

public class ReserveDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Post {
        private Long id;
        @NotBlank
        private int capacity;
        @CreatedDate
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private Date startTime;
        @CreatedDate
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private Date endTime;
    }
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Patch {
        @NotBlank
        private int capacity;
        @CreatedDate
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private Date startTime;
        @CreatedDate
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private Date endTime;
    }

    @Getter
    public static class Response implements Comparable<Response>{

        private Long reserveId;

        private Long placeId;
        private String title;
        private String address;
        private String image;
        private int capacity;

        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private Date startTime;

        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private Date endTime;

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
}
