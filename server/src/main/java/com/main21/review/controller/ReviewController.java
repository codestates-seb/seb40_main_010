package com.main21.review.controller;

import com.main21.dto.MultiResponseDto;
import com.main21.place.dto.PlaceDto;
import com.main21.review.dto.ReviewDto;
import com.main21.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {
    private final ReviewService reviewService;

    /**
     * 리뷰 등록
     * @param placeId
     * @param post
     * @param memberId
     * @return
     * @author Quartz614
     */
    @PostMapping("/{place-id}")
    public ResponseEntity postReview(@PathVariable("place-id") Long placeId,
                                     @RequestBody ReviewDto.Post post,
                                     @CookieValue(name = "memberId") Long memberId) {
        reviewService.createReview(post, memberId, placeId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * 리뷰 등록 수정
     * @param reviewId
     * @param patch
     * @param memberId
     * @return
     * @author Quartz614
     */
    @PatchMapping("/{review-id}/edit")
    public ResponseEntity patchReview(@PathVariable("review-id") Long reviewId,
                                      @RequestBody ReviewDto.Patch patch,
                                      @CookieValue(name = "memberId") Long memberId) {
        reviewService.updateReview(reviewId, patch, memberId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 리뷰 삭제
     * @param reviewId
     * @param memberId
     * @return
     * @author Quartz614
     */
    @DeleteMapping("/{review-id}")
    public ResponseEntity deleteReview(@PathVariable("review-id") Long reviewId,
                                       @CookieValue(name = "memberId") Long memberId) {
        reviewService.deleteReview(reviewId, memberId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    /**
     * 호스트 페이지에서 리뷰 조회
     * @param placeId
     * @return
     * @author Quartz614
     */
    @GetMapping("/{place-id}")
    public ResponseEntity getReserve(@PathVariable("place-id") Long placeId) {
        return new ResponseEntity(reviewService.getPlaceReviews(placeId), HttpStatus.OK);
    }

    /**
     * 마이페이지에서 등록한 리뷰 조회
     * @param pageable
     * @return
     * @author Quartz614
     */
    @GetMapping
    public ResponseEntity getReviewsMypage(@CookieValue(name = "memberId") Long memberId,
                                          Pageable pageable) {
        Page<ReviewDto.Response> getReview = reviewService.getReviewsMypage(memberId, pageable);
        List<ReviewDto.Response> reviews = getReview.getContent();

        return new ResponseEntity(new MultiResponseDto<>(reviews, getReview), HttpStatus.OK);

    }
}
