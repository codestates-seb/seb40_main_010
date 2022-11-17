package com.main21.review.repository;


import com.main21.review.dto.ReviewDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomReviewRepository {
    List<ReviewDto.Response> getReviews(Long placeId);
    Page<ReviewDto.Response> getReviewsMypage(Long memberId, Pageable pageable);
}
