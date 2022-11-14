package com.main21.review.repository;

<<<<<<< Updated upstream
import com.main21.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
=======
import com.main21.review.dto.ReviewDto;
import com.main21.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllById(Long id);
>>>>>>> Stashed changes
}
