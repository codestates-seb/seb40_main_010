package com.main21.review.service;

import com.main21.review.dto.ReviewDto;
import com.main21.review.entity.Review;
import com.main21.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    public void createReview(ReviewDto.Post post,
                               Long memberId,
                               Long placeId) {
        Review review = Review.builder()
                .score(post.getScore())
                .comment(post.getComment())
                .memberId(memberId)
                .placeId(placeId).build();
        reviewRepository.save(review);
    }
}
