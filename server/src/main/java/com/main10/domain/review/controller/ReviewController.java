package com.main10.domain.review.controller;

import com.main10.domain.review.dto.ReviewDto;
import com.main10.domain.review.service.ReviewService;
import com.main10.domain.dto.MultiResponseDto;
import com.main10.global.security.token.JwtAuthenticationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {
    private final ReviewService reviewService;

    /**
     * 리뷰 등록 컨트롤러
     *
     * @param placeId 장소 식별자
     * @param post 등록
     * @param authentication 사용자 인증 정보
     * @return ResponseEntity
     * @author Quartz614
     */
    @PostMapping("/place/{place-id}/reserve/{reserve-id}")
    public ResponseEntity<?> postReview(@PathVariable("place-id") Long placeId,
                                     @PathVariable("reserve-id") Long reserveId,
                                     @RequestBody @Valid ReviewDto.Post post,
                                     Authentication authentication) {
        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
        reviewService.createReview(post, token.getId(), placeId, reserveId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 리뷰 등록 수정 컨트롤러
     *
     * @param reviewId 리뷰 식별자
     * @param patch 수정
     * @param authentication 사용자 인증 정보
     * @return ResponseEntity
     * @author Quartz614
     */
    @PatchMapping("/{review-id}/edit")
    public ResponseEntity<?> patchReview(@PathVariable("review-id") Long reviewId,
                                      @RequestBody ReviewDto.Patch patch,
                                      Authentication authentication) {
        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
        reviewService.updateReview(reviewId, patch, token.getId());
        return ResponseEntity.ok().build();
    }

    /**
     * 리뷰 삭제 컨트롤러
     *
     * @param reviewId 리뷰 식별자
     * @param authentication 사용자 인증 정보
     * @return ResponseEntity
     * @author Quartz614
     */
    @DeleteMapping("/{review-id}")
    public ResponseEntity<?> deleteReview(@PathVariable("review-id") Long reviewId,
                                       Authentication authentication) {
        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
        reviewService.deleteReview(reviewId, token.getId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 호스트 페이지에서 리뷰 조회 컨트롤러
     *
     * @param placeId 공간 식별자
     * @return ResponseEntity
     * @author Quartz614
     */
    @GetMapping("/place/{place-id}")
    public ResponseEntity<MultiResponseDto<?>> getReview(@PathVariable("place-id") Long placeId, Pageable pageable) {
        Page<ReviewDto.Response> getReview = reviewService.getPlaceReviews(placeId, pageable);
        List<ReviewDto.Response> reviews = getReview.getContent();
        return ResponseEntity.ok(new MultiResponseDto<>(reviews, getReview));
    }

    /**
     * 마이페이지에서 작성한 리뷰 조회 컨트롤러
     *
     * @param authentication 사용자 인증 정보
     * @param pageable 페이지 정보
     * @return ResponseEntity
     * @author Quartz614
     */
    @GetMapping
    public ResponseEntity<MultiResponseDto<?>> getReviewsMypage(Authentication authentication,
                                          Pageable pageable) {
        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
        Page<ReviewDto.MyPage> getReview = reviewService.getReviewsMypage(token.getId(), pageable);
        List<ReviewDto.MyPage> reviews = getReview.getContent();
        return ResponseEntity.ok(new MultiResponseDto<>(reviews, getReview));
    }
}
