package com.main10.domain.review.service;

import com.main10.domain.review.repository.ReviewRepository;
import com.main10.global.exception.BusinessLogicException;
import com.main10.global.exception.ExceptionCode;
import com.main10.domain.review.dto.ReviewDto;
import com.main10.domain.review.entity.Review;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ReviewDbService {
    private final ReviewRepository reviewRepository;

    /**
     * 리뷰 정보 조회 메서드
     *
     * @param reviewId 리뷰 식별자
     * @return Review
     * @author Quartz614
     */
    public Review ifExistsReturnReview(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.REVIEW_NOT_FOUND));
    }

    /**
     * 호스트 페이지에서 리뷰 조회 로직
     *
     * @param placeId
     * @param pageable
     * @return Page<ReviewDto.Response>
     * @author Quartz614
     */
    public Page<ReviewDto.Response> getReviews(Long placeId, Pageable pageable) {
        return reviewRepository.getReviews(placeId, pageable);
    }

    /**
     * 마이페이지에서 등록한 리뷰 조회
     *
     * @param memberId
     * @param pageable
     * @return Page<ReviewDto.MyPage>
     * @author Quartz614
     */
    public Page<ReviewDto.MyPage> getReviewsMypage(Long memberId, Pageable pageable) {
        return reviewRepository.getReviewsMypage(memberId, pageable);
    }

    /**
     * 리뷰 저장 메서드
     *
     * @param review
     * @author quartz614
     */
    public void saveReview(Review review) {
        reviewRepository.save(review);
    }

    /**
     * 리뷰 삭제 메서드
     *
     * @param reviewId
     * @author quartz614
     */
    public void deleteReview(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }

    /**
     * 리뷰 전체 삭제 메서드
     *
     * @param placeId
     * @author quartz614
     */
    public void deleteAllByReviews(Long placeId) {
        reviewRepository.deleteAllByPlaceId(placeId);
    }

    /**
     * 리뷰 작성한 회원 수 메서드
     *
     * @param placeId
     * @return
     * @author
     */
    public Long countByPlaceId(Long placeId) {
        return reviewRepository.countByPlaceId(placeId);
    }
}
