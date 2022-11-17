package com.main21.review.service;

import com.main21.exception.BusinessLogicException;
import com.main21.exception.ExceptionCode;
import com.main21.place.entity.Place;
import com.main21.review.dto.ReviewDto;
import com.main21.review.entity.Review;
import com.main21.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor

public class ReviewService {
    private final ReviewRepository reviewRepository;

    /**
     * 리뷰 등록 로직
     * @param post
     * @param memberId
     * @param placeId
     * @author Quartz614
     */
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

    /**
     * 리뷰 수정 로직
     * @param reviewId
     * @param patch
     * @param memberId
     * @author Quartz614
     */
    public void updateReview (Long reviewId, ReviewDto.Patch patch, Long memberId){
        Review findReview = reviewRepository
                .findById(reviewId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.REVIEW_NOT_FOUND));
        if (!memberId.equals(findReview.getMemberId()))
            throw new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND);

        findReview.editReview(patch.getScore(),patch.getComment());
        reviewRepository.save(findReview);
    }

    /**
     * 호스트 페이지에서 리뷰 조회 로직
     * @param placeId
     * @return
     * @author Quartz614
     */
    public List<ReviewDto.Response> getPlaceReviews (Long placeId) {
        return reviewRepository.getReviews(placeId);
    }

    /**
     * 리뷰 삭제 로직
     * @param reviewId
     * @param memberId
     * @author Quartz614
     */
    public void deleteReview (Long reviewId, Long memberId) {
        Review findReview = reviewRepository
                .findById(reviewId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.REVIEW_NOT_FOUND));
        if (!memberId.equals(findReview.getMemberId()))
            throw new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND);

        reviewRepository.deleteById(reviewId);
    }

    /**
     * 마이페이지에서 등록한 리뷰 조회
     * @param memberId
     * @param pageable
     * @return
     * @author Quartz614
     */
    public Page<ReviewDto.Response> getReviewsMypage(Long memberId, Pageable pageable) {
        return reviewRepository.getReviewsMypage(memberId, pageable);
    }
}

