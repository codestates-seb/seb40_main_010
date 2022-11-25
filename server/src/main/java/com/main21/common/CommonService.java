package com.main21.common;

import com.main21.exception.BusinessLogicException;
import com.main21.exception.ExceptionCode;
import com.main21.member.repository.MemberRepository;
import com.main21.place.entity.Place;
import com.main21.place.repository.PlaceRepository;
import com.main21.batch.entity.MbtiCount;
import com.main21.reserve.entity.Reserve;
import com.main21.batch.repository.MbtiCountRepository;
import com.main21.reserve.repository.ReserveRepository;
import com.main21.review.entity.Review;
import com.main21.review.repository.ReviewRepository;
import com.main21.security.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 사용이 빈번한 메서드 공통화 작업<br>
 * 2022-11-22
 * @author mozzi327
 */

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CommonService {
    private final RedisUtils redisUtils;
    private final PlaceRepository placeRepository;
    private final MemberRepository memberRepository;
    private final ReserveRepository reserveRepository;
    private final MbtiCountRepository mbtiCountRepository;
    private final ReviewRepository reviewRepository;

    /**
     * 예약 정보 조회 메서드
     *
     * @param reserveId 예약 식별자
     * @return Reserve
     * @author mozzi327
     */
    public Reserve ifExistsReturnReserve(Long reserveId) {
        return reserveRepository.findById(reserveId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.RESERVATION_NOT_FOUND));
    }





    /**
     * 호스팅 정보 조회 메서드
     *
     * @param placeId 장소 식별자
     * @return Place
     * @author mozzi327
     */
    public Place ifExistsReturnPlace(Long placeId) {
        return placeRepository.findById(placeId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.PLACE_NOT_FOUND));
    }

    /**
     * 리뷰 정보 조회 메서드
     *
     * @param reviewId 리뷰 식별자
     * @return Review
     * @author Quartz614
     */
    public Review ifExistsReturnReview(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.REVIEW_NOT_FOUND));
    }


    /**
     * MBTICount 정보 조회 메서드
     *
     * @param mbti    MBTI
     * @param placeId 장소 식별자
     * @return MBTICount
     * @author mozzi327
     */
    public MbtiCount ifExistsReturnMBTICount(String mbti, Long placeId) {
        return mbtiCountRepository.findMbtiCountByMbtiAndPlaceId(mbti, placeId)
                .orElse(null);
    }






    /**
     * 리프레시 토큰을 통해 사용자 식별자를 조회하는 메서드
     * @param refreshToken 리프레시 토큰
     * @return Long
     * @author mozzi327
     */
    public Long getIdForRefresh(String refreshToken) {
        return redisUtils.getId(refreshToken);
    }
}
