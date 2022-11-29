package com.main21.review.service;

import com.main21.exception.BusinessLogicException;
import com.main21.exception.ExceptionCode;
import com.main21.place.entity.Place;
import com.main21.place.service.PlaceDbService;
import com.main21.reserve.entity.Reserve;
import com.main21.reserve.service.ReserveDbService;
import com.main21.review.dto.ReviewDto;
import com.main21.review.entity.Review;
import com.main21.security.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static com.main21.reserve.entity.Reserve.ReserveStatus.PAY_IN_PROGRESS;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final PlaceDbService placeDbService;
    private final RedisUtils redisUtils;
    private final ReserveDbService  reserveDbService;
    private final ReviewDbService reviewDbService;

    /**
     * 리뷰 등록 로직
     *
     * @param post         등록
     * @param refreshToken 리프래시 토큰
     * @param placeId      장소 식별자
     * @author Quartz614
     */
    public void createReview(ReviewDto.Post post,
                             String refreshToken,
                             Long placeId,
                             Long reserveId) {
        Long memberId = redisUtils.getId(refreshToken);
        Reserve findReserve = reserveDbService.ifExistsReturnReserve(reserveId);

        // 예약 상태가 결제 진행중이 아니라면 리뷰 달지 못함
        if (!findReserve.getStatus().equals(PAY_IN_PROGRESS)) {
            throw new BusinessLogicException(ExceptionCode.RESERVATION_NOT_FOUND);
        }
        Review review = Review.builder()
                .score(post.getScore())
                .comment(post.getComment())
                .memberId(memberId)
                .placeId(placeId).build();
        reviewDbService.saveReview(review);
        Place findPlace = placeDbService.ifExistsReturnPlace(placeId);

        modifyScore(findPlace, findPlace.getTotalScore() + review.getScore(), placeId);
    }

    /**
     * 리뷰 수정 로직
     *
     * @param reviewId     리뷰 식별자
     * @param patch        수정
     * @param refreshToken 리프래시 토큰
     * @author Quartz614
     */
    public void updateReview(Long reviewId, ReviewDto.Patch patch, String refreshToken) {
        Long memberId = redisUtils.getId(refreshToken);
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
     * @param placeId
     * @return Page<ReviewDto.Response>
     * @author Quartz614
     */
    public Page<ReviewDto.Response> getPlaceReviews(Long placeId, Pageable pageable) {
        return reviewDbService.getReviews(placeId, pageable);
    }

    /**
     * 리뷰 삭제 로직 메서드
     *
     * @param reviewId     리뷰 식별자
     * @param refreshToken 리프래시 토큰
     * @author Quartz614
     */
    public void deleteReview(Long reviewId, String refreshToken) {
        Long memberId = redisUtils.getId(refreshToken);
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
     * @param refreshToken 리프래시 토큰
     * @param pageable 페이지 정보
     * @return Page<ReviewDto.MyPage>
     * @author Quartz614
     */
    public Page<ReviewDto.MyPage> getReviewsMypage(String refreshToken, Pageable pageable) {
        Long memberId = redisUtils.getId(refreshToken);
        return reviewDbService.getReviewsMypage(memberId, pageable);
    }

    /**
     *
     * @param findPlace
     * @param totalScore
     * @param placeId
     * @author LimJaeminZ
     */
    public void modifyScore(Place findPlace, Double totalScore, Long placeId) {
        Long reviewer = reviewDbService.countByPlaceId(placeId);
        String str = String.format("%.2f", totalScore / reviewer);
        double score = Double.parseDouble(str);
        findPlace.setTotalScore(totalScore);
        findPlace.setScore(score);
        placeDbService.savePlace(findPlace);
    }
}

