package com.main21.review.service;

import com.main21.exception.BusinessLogicException;
import com.main21.exception.ExceptionCode;
import com.main21.review.entity.Review;
import com.main21.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

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
}
