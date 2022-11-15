package com.main21.review.dto;

import com.main21.member.entity.Member;
import com.main21.review.entity.Review;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

import static com.main21.review.entity.QReview.review;

public class ReviewDto {
    @Getter
    public static class Post {
        private double score;

        private String comment;
    }
    @Getter
    public static class Patch {
        private Double score;

        private String comment;
    }

    @Getter

    public static class Response implements Comparable<Response> {
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


        /**
         * 최신순 정렬 메서드
         * @param o Response
         * @return 1 or -1
         * @author quartz614
         */
        @Override
        public int compareTo(Response o) {
            return (int)(this.reviewId-o.reviewId);
        }
    }

    @Getter
    public static class MyPage implements Comparable<MyPage> {
        private final Long reviewId;

        private final String title;
        private final Double score;
        private final String comment;
        private final LocalDateTime createdAt;

        @Builder
        public MyPage(Long reviewId, String title, Double score, String comment, LocalDateTime createdAt) {
            this.reviewId = reviewId;
            this.title = title;
            this.score = score;
            this.comment = comment;
            this.createdAt = createdAt;
        }
        /**
         * 최신순 정렬 메서드
         * @param o MyPage
         * @return 1 or -1
         * @author quartz614
         */
        @Override
        public int compareTo(MyPage o) {
            return (int)(this.reviewId-o.reviewId);
        }
    }
}
