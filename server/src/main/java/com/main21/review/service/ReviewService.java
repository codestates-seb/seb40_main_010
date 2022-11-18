package com.main21.review.service;

import com.main21.exception.BusinessLogicException;
import com.main21.exception.ExceptionCode;
import com.main21.place.entity.Place;
import com.main21.place.repository.PlaceRepository;
import com.main21.review.dto.ReviewDto;
import com.main21.review.entity.Review;
import com.main21.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor

public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final PlaceRepository placeRepository;

    /**
     * 리뷰 등록 로직
     * @param post
     * @param memberId
     * @param placeId
     * @author Quartz614
     */
    public void createReview(ReviewDto.Post post,
                             Long memberId,
                             Long placeId) {
        Review review = Review.builder()
                .score(post.getScore())
                .comment(post.getComment())
                .memberId(memberId)
                .placeId(placeId).build();
        reviewRepository.save(review);
        Place findPlace = placeRepository.findById(placeId).orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.PLACE_NOT_FOUND));

        modifyScore(findPlace, findPlace.getTotalScore()+review.getScore(), placeId);
    }

    /**
     * 리뷰 수정 로직
     * @param reviewId
     * @param patch
     * @param memberId
     * @author Quartz614
     */
    public void updateReview (Long reviewId, ReviewDto.Patch patch, Long memberId){
        Review findReview = reviewRepository
                .findById(reviewId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.REVIEW_NOT_FOUND));
        if (!memberId.equals(findReview.getMemberId()))
            throw new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND);

        Place findPlace = placeRepository.findById(findReview.getPlaceId()).orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.PLACE_NOT_FOUND));

        findPlace.setTotalScore(findPlace.getTotalScore() - findReview.getScore());
        placeRepository.save(findPlace);

        findReview.editReview(patch.getScore(),patch.getComment());
        reviewRepository.save(findReview);

        modifyScore(findPlace, findPlace.getTotalScore()+findReview.getScore(), findReview.getPlaceId());
    }

    /**
     * 호스트 페이지에서 리뷰 조회 로직
     * @param placeId
     * @return
     * @author Quartz614
     */
    public Page<ReviewDto.Response> getPlaceReviews (Long placeId, Pageable pageable) {
        return reviewRepository.getReviews(placeId, pageable);
    }

    /**
     * 리뷰 삭제 로직
     * @param reviewId
     * @param memberId
     * @author Quartz614
     */
    public void deleteReview (Long reviewId, Long memberId) {
        Review findReview = reviewRepository
                .findById(reviewId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.REVIEW_NOT_FOUND));
        if (!memberId.equals(findReview.getMemberId()))
            throw new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND);

        Place findPlace = placeRepository.findById(findReview.getPlaceId()).orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.PLACE_NOT_FOUND));

        findPlace.setTotalScore(findPlace.getTotalScore() - findReview.getScore());
        placeRepository.save(findPlace);

        reviewRepository.deleteById(reviewId);

        modifyScore(findPlace, findPlace.getTotalScore(), findPlace.getId());

    }

    /**
     * 마이페이지에서 등록한 리뷰 조회
     * @param memberId
     * @param pageable
     * @return
     * @author Quartz614
     */
    public Page<ReviewDto.Response> getReviewsMypage(Long memberId, Pageable pageable) {
        return reviewRepository.getReviews(memberId, pageable);
    }

    private void modifyScore(Place findPlace, Double totalScore, Long placeId) {
        Long reviewer = reviewRepository.countByPlaceId(placeId);
        String str = String.format("%.2f", totalScore / reviewer);
        double score = Double.parseDouble(str);
        findPlace.setTotalScore(totalScore);
        findPlace.setScore(score);
        placeRepository.save(findPlace);
    }
}

