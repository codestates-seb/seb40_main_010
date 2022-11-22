package com.main21.review.repository;

import com.main21.place.entity.Place;
import com.main21.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long>, CustomReviewRepository {
    List<Review> findAllById(Long id);
    List<Review> findAllByPlaceId(Long placeId);
    Long countByPlaceId(Long placeId);
    List<Review> deleteAllByPlaceId(Long placeId);

}
