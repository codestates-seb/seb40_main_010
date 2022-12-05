package com.main10.domain.review.service;

import com.main10.domain.review.dto.ReviewDto;
import com.main10.domain.review.entity.Review;
import com.main10.global.exception.BusinessLogicException;
import com.main10.global.exception.ExceptionCode;
import com.main10.domain.place.entity.Place;
import com.main10.domain.place.service.PlaceDbService;
import com.main10.domain.reserve.entity.Reserve;
import com.main10.domain.reserve.service.ReserveDbService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final PlaceDbService placeDbService;
    private final ReserveDbService  reserveDbService;
    private final ReviewDbService reviewDbService;

    /**
     * 리뷰 등록 로직
     *
     * @param post         등록
     * @param memberId     사용자 식별자
     * @param placeId      장소 식별자
     * @param reserveId    예약 식별자
     * @author Quartz614
     */
    public void createReview(ReviewDto.Post post,
                             Long memberId,
                             Long placeId,
                             Long reserveId) {
        Place findPlace = placeDbService.ifExistsReturnPlace(placeId);

        if (findPlace.getMemberId().equals(memberId)) {
            throw new BusinessLogicException(ExceptionCode.HOST_CANNOT_REVIEW);
        }

        Reserve findReserve = reserveDbService.ifExistsReturnReserve(reserveId);

        // 결제 완료 상태가 아니라면 리뷰 달지 못함
        if (!findReserve.getStatus().equals(Reserve.ReserveStatus.PAY_SUCCESS)) {
            throw new BusinessLogicException(ExceptionCode.RESERVATION_NOT_FOUND);
        }

        Review review = Review.builder()
                .score(post.getScore())
                .comment(post.getComment())
                .memberId(memberId)
                .placeId(placeId).build();
        reviewDbService.saveReview(review);

        modifyScore(findPlace, findPlace.getTotalScore() + review.getScore(), placeId);
    }

    /**
     * 리뷰 수정 로직
     *
     * @param reviewId     리뷰 식별자
     * @param patch        수정
     * @param memberId     사용자 식별자
     * @author Quartz614
     */
    public void updateReview(Long reviewId, ReviewDto.Patch patch, Long memberId) {
        Review findReview = reviewDbService.ifExistsReturnReview(reviewId);

        if (!memberId.equals(findReview.getMemberId()))
            throw new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND);

        Place findPlace = placeDbService.ifExistsReturnPlace(findReview.getPlaceId());

        findPlace.setTotalScore(findPlace.getTotalScore() - findReview.getScore());
        placeDbService.savePlace(findPlace);

        findReview.editReview(patch.getScore(), patch.getComment());
        reviewDbService.saveReview(findReview);

        modifyScore(findPlace, findPlace.getTotalScore() + findReview.getScore(), findReview.getPlaceId());
    }

    /**
     * 호스트 페이지에서 리뷰 조회 로직
     *
     * @param placeId 사용자 식별자
     * @param pageable 페이지 정보
     * @return Page(ReviewDto.Response)
     * @author Quartz614
     */
    public Page<ReviewDto.Response> getPlaceReviews(Long placeId, Pageable pageable) {
        return reviewDbService.getReviews(placeId, pageable);
    }

    /**
     * 리뷰 삭제 로직 메서드
     *
     * @param reviewId 리뷰 식별자
     * @param memberId 사용자 식별자
     * @author Quartz614
     */
    public void deleteReview(Long reviewId, Long memberId) {
        Review findReview = reviewDbService.ifExistsReturnReview(reviewId);

        if (!memberId.equals(findReview.getMemberId()))
            throw new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND);

        Place findPlace = placeDbService.ifExistsReturnPlace(findReview.getPlaceId());
        findPlace.setTotalScore(findPlace.getTotalScore() - findReview.getScore());

        placeDbService.savePlace(findPlace);
        reviewDbService.deleteReview(reviewId);

        modifyScore(findPlace, findPlace.getTotalScore(), findPlace.getId());

    }

    /**
     * 마이페이지에서 등록한 리뷰 조회 메서드
     *
     * @param memberId 사용자 식별자
     * @param pageable 페이지 정보
     * @return Page(ReviewDto.MyPage)
     * @author Quartz614
     */
    public Page<ReviewDto.MyPage> getReviewsMypage(Long memberId, Pageable pageable) {
        return reviewDbService.getReviewsMypage(memberId, pageable);
    }

    /**
     * 장소 평점 계산 메서드
     *
     * @param findPlace 조회된 장소 정보
     * @param totalScore 등록된 총 평점
     * @param placeId 장소 식별자
     * @author LimJaeminZ
     */
    public void modifyScore(Place findPlace, Double totalScore, Long placeId) {
        Long reviewer = reviewDbService.countByPlaceId(placeId);
        String str = String.format("%.2f", totalScore / reviewer);
        double score = Double.parseDouble(str);
        if(Double.isNaN(score)) {
            findPlace.setTotalScore(totalScore);
            findPlace.setScore(0);
            placeDbService.savePlace(findPlace);
        } else {
            findPlace.setTotalScore(totalScore);
            findPlace.setScore(score);
            placeDbService.savePlace(findPlace);
        }
    }
}

