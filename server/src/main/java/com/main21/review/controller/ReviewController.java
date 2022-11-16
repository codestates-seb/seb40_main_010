package com.main21.review.controller;

import com.main21.review.dto.ReviewDto;
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

    @PostMapping("/{place-id}")
    public ResponseEntity postReview(@PathVariable("place-id") Long placeId,
                                     @RequestBody ReviewDto.Post post,
                                     @CookieValue(name = "memberId") Long memberId) {
        reviewService.createReview(post, memberId, placeId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping("/{review-id}/edit")
    public ResponseEntity patchReview(@PathVariable("review-id") Long reviewId,
                                      @RequestBody ReviewDto.Patch patch,
                                      @CookieValue(name = "memberId") Long memberId) {
        reviewService.updateReview(reviewId, patch, memberId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{review-id}")
    public ResponseEntity deleteReview(@PathVariable("review-id") Long reviewId,
                                       @CookieValue(name = "memberId") Long memberId) {
        reviewService.deleteReview(reviewId, memberId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
    @GetMapping("/{place-id}")
    public ResponseEntity getReserve(@PathVariable("place-id") Long placeId) {
        return new ResponseEntity(reviewService.getPlaceReviews(placeId), HttpStatus.OK);
    }
}
