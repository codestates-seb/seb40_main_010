package com.main21.review.service;

import com.main21.exception.BusinessLogicException;
import com.main21.exception.ExceptionCode;
import com.main21.place.entity.Place;
import com.main21.review.dto.ReviewDto;
import com.main21.review.entity.Review;
import com.main21.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public void updateReview (Long reviewId, ReviewDto.Patch patch){
        Review findReview = reviewRepository.findById(reviewId).orElseThrow(() -> new BusinessLogicException(ExceptionCode.REVIEW_NOT_FOUND));
        findReview.editReview(patch.getScore(),patch.getComment());
        reviewRepository.save(findReview);
    }
    public List<Review> getPlaceReviews (Long placeId) {
        List<Review> findReview = reviewRepository.findAllByPlaceId(placeId);
        return findReview;

    }

    public void deleteReview (Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }
}

