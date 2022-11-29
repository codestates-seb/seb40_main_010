package com.main21.reserve.service;

import com.main21.batch.service.MbtiCountService;
import com.main21.exception.BusinessLogicException;
import com.main21.exception.ExceptionCode;
import com.main21.member.entity.Member;
import com.main21.member.service.MemberDbService;
import com.main21.place.entity.Place;
import com.main21.place.service.PlaceDbService;
import com.main21.reserve.dto.*;
import com.main21.reserve.entity.*;
import com.main21.reserve.pay.KakaoHeaders;
import com.main21.reserve.pay.PayApproveInfo;
import com.main21.reserve.pay.PayReadyInfo;
import com.main21.reserve.pay.ReadyToPaymentInfo;
import com.main21.reserve.pay.RequestForReserveInfo;
import com.main21.security.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static com.main21.reserve.utils.PayConstants.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReserveService {

    private final RedisUtils redisUtils;
    private final FeignService feignService;
    private final PlaceDbService placeDbService;
    private final MemberDbService memberDbService;
    private final MbtiCountService mbtiCountService;
    private final ReserveDbService reserveDbService;


    /**
     * 예약 프로세스 1 - 예약 등록 : 예약 등록 메서드
     *
     * @param post         예약 등록 정보
     * @param placeId      장소 식별자
     * @param refreshToken 리프레시 토큰
     * @author LimJaeminZ
     */
    @Transactional
    public void createReserve(ReserveDto.Post post, Long placeId, String refreshToken) {
        Long memberId = redisUtils.getId(refreshToken);
        Place findPlace = placeDbService.ifExistsReturnPlace(placeId);

//        if (memberId.equals(findPlace.getMemberId()))
//            throw new BusinessLogicException(ExceptionCode.HOST_CANNOT_RESERVATION);

        Reserve reserve = Reserve.builder()
                .capacity(post.getCapacity())
                .startTime(post.getStartTime())
                .endTime(post.getEndTime())
                .placeId(findPlace.getId())
                .memberId(memberId)
                .totalCharge((long)(post.getEndTime().getHour() - post.getStartTime().getHour()) * findPlace.getCharge())
                .build();

        // 최대 수용인원 초과 예약 금지
        if (reserve.getCapacity() > findPlace.getMaxCapacity()) {
            throw new BusinessLogicException(ExceptionCode.RESERVATION_MAX_CAPACITY_OVER);
        } else reserveDbService.saveReserve(reserve);
    }


    /**
     * 예약 프로세스 2 - 결제 진행중 : 결제 URL 요청 메서드
     *
     * @param reserveId    예약 식별자
     * @param refreshToken
     * @param requestUrl   요청 URL
     * @return URL(결제 페이지)
     * @author mozzi327
     */
    @Transactional
    public String getKaKaoPayUrl(Long reserveId,
                                 String refreshToken,
                                 String requestUrl) {

        /*  Todo : 예약 가능한지 확인하는 이벤트 처리(논의중)  */

        Long memberId = redisUtils.getId(refreshToken);

        // 사용자, 예약, 호스트 정보 조회
        Member findMember = memberDbService.ifExistsReturnMember(memberId);
        Reserve findReserve = reserveDbService.ifExistsReturnReserve(reserveId);
        Place findPlace = placeDbService.ifExistsReturnPlace(findReserve.getPlaceId());

        // 결제 페이지 요청을 위한 headers, params 세팅
        KakaoHeaders headers = feignService.setHeaders();
        ReadyToPaymentInfo params = feignService.setReadyParams(requestUrl, findMember, findReserve, findPlace);

        // feign client 요청(결제페이지 정보)
        PayReadyInfo payReadyDto = feignService.getPaymentUrlResponse(headers, params);

        // 성공적으로 결제 창 요청이 왔다면 해당 데이터를 예약 정보에 저장
        findReserve.setPaymentInfo(params, payReadyDto.getTid());
        reserveDbService.saveReserve(findReserve);

        return payReadyDto.getNextRedirectPcUrl();
    }


    /**
     * 예약 프로세스 3 - 결제 완료 : 예약 정보 반환 메서드
     *
     * @param pgToken Payment Gateway Token
     * @return PayApprovalDto
     * @author mozzi327
     */
    @Transactional
    public PayApproveInfo getApprovedKaKaoPayInfo(Long reserveId,
                                                  String pgToken) {
        Reserve findReserve = reserveDbService.ifExistsReturnReserve(reserveId);
        Member findMember = memberDbService.ifExistsReturnMember(findReserve.getMemberId());

        // 예약 정보 반환을 위한 headers, params 세팅
        KakaoHeaders headers = feignService.setHeaders();
        RequestForReserveInfo params = feignService.setRequestParams(pgToken, findReserve);

        // feign client 요청(예약 정보)
        PayApproveInfo approvalDto = feignService.getSuccessResponse(headers, params);

        // 결제 성공시 예약 상태 변경 및 DTO에 메시지를 담는 과정
        approvalDto.setOrderStatus(ORDER_APPROVED);
        findReserve.setStatus(Reserve.ReserveStatus.PAY_SUCCESS);
        reserveDbService.saveReserve(findReserve);

        mbtiCountService.addMbtiCount(findMember, findReserve.getPlaceId());

        return approvalDto;
    }


    /**
     * 예약 프로세스 4 - 결제 취소 시 결제 취소 상태 변경 메서드
     *
     * @author mozzi327
     */
    public void setCanceledStatus(Long reserveId) {
        Reserve findReserve = reserveDbService.ifExistsReturnReserve(reserveId);
        findReserve.setStatus(Reserve.ReserveStatus.PAY_CANCELED);
        reserveDbService.saveReserve(findReserve);
    }


    /**
     * 예약 프로세스 5 - 결제 실패 시 결제 실패 상태 변경 메서드
     *
     * @author mozzi327
     */
    public void setFailedStatus(Long reserveId) {
        Reserve findReserve = reserveDbService.ifExistsReturnReserve(reserveId);
        findReserve.setStatus(Reserve.ReserveStatus.PAY_FAILED);
        reserveDbService.saveReserve(findReserve);
    }


    /* ------------------------------------ 예약 프로세스 끝 -------------------------------------*/


    /**
     * 예약 전체 조회 메서드
     *
     * @return List
     * @Param refreshToken
     * @author LeeGoh
     */
    public Page<ReserveDto.Response> getReservation(String refreshToken, Pageable pageable) {
        Long memberId = redisUtils.getId(refreshToken);
        return reserveDbService.getReservation(memberId, pageable);
    }


    /**
     * 예약 삭제 메서드
     * 예약 삭제 시 취소 사유 작성
     * 예약 취소 시 mbtiCount -1, spaceCount -1
     *
     * @param reserveId    예약 식별자
     * @param refreshToken
     * @author LeeGoh
     */
    public void deleteReserve(Long reserveId, String refreshToken) {
        Long memberId = redisUtils.getId(refreshToken);

        // 예약 조회
        Reserve findReserve = reserveDbService.ifExistsReturnReserve(reserveId);

        // 회원 조회
        Member findMember = memberDbService.ifExistsReturnMember(memberId);

        if (findReserve.getStatus().equals(Reserve.ReserveStatus.RESERVATION_CANCELED)) {
            throw new BusinessLogicException(ExceptionCode.RESERVATION_NOT_FOUND);
        }

        // mbtiCount -1
        mbtiCountService.reduceMbtiCount(findMember, findReserve.getPlaceId());

        // 예약 상태 변경 ... -> RESERVATION_CANCELED
        findReserve.setStatus(Reserve.ReserveStatus.RESERVATION_CANCELED);
        reserveDbService.saveReserve(findReserve);
    }






    /* ------------------------------------ 일단 예외 -------------------------------------*/





    /**
     * 예약 수정 메서드(사용 미정)
     *
     * @param patch        에약 수정 정보
     * @param reserveId    예약 식별자
     * @param refreshToken
     * @author Quartz614
     */
    public void updateReserve(ReserveDto.Patch patch, Long reserveId, String refreshToken) {
        Long memberId = redisUtils.getId(refreshToken);
        Reserve findReserve = reserveDbService.ifExistsReturnReserve(reserveId); // 추후 수정
        if (!memberId.equals(findReserve.getMemberId()))
            throw new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND);

        findReserve.editReserve(patch.getCapacity(), patch.getStartTime(), patch.getEndTime());
        reserveDbService.saveReserve(findReserve);
    }









    /* ------------------------------------ Common Method --------------------------------------*/


    /**
     * OrderId를 파싱하는 메서드
     *
     * @param orderId 카카오 주문 정보 식별자
     * @return String[]
     * @author mozzi327
     */
    private String[] parseOrderInfo(String orderId) {
        return orderId.split("/");
    }
}

