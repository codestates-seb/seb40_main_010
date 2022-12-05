package com.main10.domain.review.repository;


import com.main10.domain.review.dto.ReviewDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomReviewRepository {
    Page<ReviewDto.Response> getReviews(Long placeId, Pageable pageable);
    Page<ReviewDto.MyPage> getReviewsMypage(Long memberId, Pageable pageable);

}
