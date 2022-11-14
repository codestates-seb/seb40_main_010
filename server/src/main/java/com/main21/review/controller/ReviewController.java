package com.main21.review.controller;

import com.main21.review.dto.ReviewDto;
import com.main21.review.entity.Review;
import com.main21.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/{place-Id}")
    public ResponseEntity postReview(@PathVariable("place-Id") Long placeId, @RequestBody ReviewDto.Post reviewDtoPost,
                                     @CookieValue(name = "memberId") Long memberId) {
        reviewService.createReview(reviewDtoPost, memberId, placeId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
