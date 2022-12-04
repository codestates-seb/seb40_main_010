package com.main10.domain.review.dto;

import com.main10.domain.member.entity.Member;
import com.main10.domain.place.entity.Place;
import com.main10.domain.review.entity.Review;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

public class ReviewDto {

    @Getter
    @NoArgsConstructor
    public static class Post {
        @NotNull
        @Positive(message = "평점은 음수가 될 수 없습니다.")
        private double score;

        @NotBlank
        @Length(min = 5, max = 300, message = "글자 수는 최소 5자 이상 최대 300자 이하여야 합니다.")
        private String comment;

        @Builder
        public Post(double score, String comment) {
            this.score = score;
            this.comment = comment;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class Patch {
        @NotNull
        @Positive(message = "평점은 음수가 될 수 없습니다.")
        private Double score;

        @NotBlank
        @Length(min = 5, max = 300, message = "글자 수는 최소 5자 이상 최대 300자 이하여야 합니다.")
        private String comment;

        @Builder
        public Patch(Double score, String comment) {
            this.score = score;
            this.comment = comment;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class Response {
        private Long reviewId;
        private String nickname;
        private Double score;
        private String comment;
        private LocalDateTime createdAt;
        private String profileImage;

        @Builder
        @QueryProjection
        public Response(Review review, Member member) {
            this.reviewId = review.getId();
            this.nickname = member.getNickname();
            this.score = review.getScore();
            this.comment = review.getComment();
            this.createdAt = review.getCreatedAt();
            this.profileImage = member.getMemberImage().getFilePath();
        }
    }

    @Getter
    @NoArgsConstructor
    public static class MyPage {
        private Long reviewId;
        private String title;
        private Double score;
        private String comment;
        private LocalDateTime createdAt;
        private Long placeId;
        private String image;

        @Builder
        @QueryProjection
        public MyPage(Review review, Place place) {
            this.reviewId = review.getId();
            this.title = place.getTitle();
            this.score = review.getScore();
            this.comment = review.getComment();
            this.createdAt = review.getCreatedAt();
            this.placeId = place.getId();
            this.image = place.getPlaceImages().get(0).getFilePath();
        }
    }
}
