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

import static com.main21.member.utils.AuthConstant.REFRESH_TOKEN;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {
    private final ReviewService reviewService;

    /**
     * 리뷰 등록
     * @param placeId 장소 식별자
     * @param post 등록
     * @param refreshToken 리프래시 토큰
     * @return
     * @author Quartz614
     */
    @PostMapping("/{place-id}/reserve/{reserve-id}")
    public ResponseEntity postReview(@PathVariable("place-id") Long placeId,
                                     @PathVariable("reserve-id") Long reserveId,
                                     @RequestBody ReviewDto.Post post,
                                     @RequestHeader(name = REFRESH_TOKEN) String refreshToken) {
        reviewService.createReview(post, refreshToken, placeId, reserveId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * 리뷰 등록 수정
     * @param reviewId 리뷰 식별자
     * @param patch 수정
     * @param refreshToken 리프래시 토큰
     * @return
     * @author Quartz614
     */
    @PatchMapping("/{review-id}/edit")
    public ResponseEntity patchReview(@PathVariable("review-id") Long reviewId,
                                      @RequestBody ReviewDto.Patch patch,
                                      @RequestHeader(name = REFRESH_TOKEN) String refreshToken) {
        reviewService.updateReview(reviewId, patch, refreshToken);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 리뷰 삭제
     * @param reviewId 리뷰 식별자
     * @param refreshToken 리프래시 토큰
     * @return
     * @author Quartz614
     */
    @DeleteMapping("/{review-id}")
    public ResponseEntity deleteReview(@PathVariable("review-id") Long reviewId,
                                       @RequestHeader(name = REFRESH_TOKEN) String refreshToken) {
        reviewService.deleteReview(reviewId, refreshToken);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    /**
     * 호스트 페이지에서 리뷰 조회
     * @param placeId
     * @return
     * @author Quartz614
     */
    @GetMapping("/{place-id}")
    public ResponseEntity getReview(@PathVariable("place-id") Long placeId, Pageable pageable) {
        Page<ReviewDto.Response> getReview = reviewService.getPlaceReviews(placeId, pageable);
        List<ReviewDto.Response> reviews = getReview.getContent();

        return new ResponseEntity(new MultiResponseDto<>(reviews, getReview), HttpStatus.OK);
    }

    /**
     * 마이페이지에서 작성한 리뷰 조회
     * @param refreshToken 리프래시 토큰
     * @param pageable 페이징 처리
     * @return
     * @author Quartz614
     */

    @GetMapping
    public ResponseEntity getReviewsMypage(@RequestHeader(name = REFRESH_TOKEN) String refreshToken,
                                          Pageable pageable) {
        Page<ReviewDto.MyPage> getReview = reviewService.getReviewsMypage(refreshToken, pageable);
        List<ReviewDto.MyPage> reviews = getReview.getContent();

        return new ResponseEntity(new MultiResponseDto<>(reviews, getReview), HttpStatus.OK);
    }
}
