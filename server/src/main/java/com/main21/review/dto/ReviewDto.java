package com.main21.review.dto;

import com.main21.member.entity.Member;
import com.main21.place.entity.Place;
import com.main21.review.entity.Review;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class ReviewDto {
    @Getter
    public static class Post {
        @NotBlank
        private double score;
        @NotBlank
        private String comment;
    }
    @Getter
    public static class Patch {
        @NotBlank
        private Double score;
        @NotBlank
        private String comment;
    }

    @Getter

    public static class Response {
        private final Long reviewId;

        private final String profileImage;

        private final String nickname;

        private final Double score;

        private final String comment;

        private final LocalDateTime createdAt;



        @Builder
        @QueryProjection
        public Response(Review review, Member member) {
            this.reviewId = review.getId();
            this.profileImage = member.getProfileImage();
            this.nickname = member.getNickname();
            this.score = review.getScore();
            this.comment = review.getComment();
            this.createdAt = review.getCreatedAt();
        }
    }

    @Getter
    public static class MyPage {
        private final Long reviewId;

        private final String title;
        private final Double score;
        private final String comment;
        private final LocalDateTime createdAt;

        private final Long placeId;

        @Builder
        @QueryProjection
        public MyPage(Review review, Place place) {
            this.reviewId = review.getId();
            this.title = place.getTitle();
            this.score = review.getScore();
            this.comment = review.getComment();
            this.createdAt = review.getCreatedAt();
            this.placeId = place.getId();
        }
    }
}
