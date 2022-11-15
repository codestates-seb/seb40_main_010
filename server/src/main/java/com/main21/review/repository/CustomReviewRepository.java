package com.main21.review.repository;


import com.main21.review.dto.ReviewDto;

import java.util.List;

public interface CustomReviewRepository {
    List<ReviewDto.Response> getReviews(Long placeId);
}
