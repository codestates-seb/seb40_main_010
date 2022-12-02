package com.main10.domain.review.repository;

import com.main10.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long>, CustomReviewRepository {
    Long countByPlaceId(Long placeId);
    List<Review> deleteAllByPlaceId(Long placeId);

}
