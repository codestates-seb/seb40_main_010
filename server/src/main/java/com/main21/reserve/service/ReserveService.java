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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static com.main21.reserve.utils.PayConstants.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReserveService {

    @Value("${kakao.admin.key}")
    private String adminKey;

    @Value("${kakao.host}")
    private String host;

    @Value("${kakao.uri.approval}")
    private String approvalUri;

    @Value("${kakao.uri.cancel}")
    private String cancelUri;

    @Value("${kakao.uri.fail}")
    private String failUri;

    @Value("${kakao.pay.ready}")
    private String kakaoPayReady;

    @Value("${kakao.pay.approve}")
    private String kakaoPayApprove;

    @Value("${kakao.pay.cid}")
    private String testCid;

    @Value("${kakao.pay.taxfree}")
    private Integer taxFreeAmount;

    @Value("${kakao.pay.cancel}")
    private String kakaoPayCancel;

    @Value("${kakao.pay.order}")
    private String kakaoPayOrder;

    private final RedisUtils redisUtils;
    private final FeignService feignService;
    private final PlaceDbService placeDbService;
    private final MemberDbService memberDbService;
    private final MbtiCountService mbtiCountService;
    private final ReserveDbService reserveDbService;

    private long quantity;
    private String userId;
    private String orderId;
    private String itemName;
    private Long totalAmount;
    private PayReadyInfo payReadyDto;


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

        //공간 확인
        Place findPlace = placeDbService.ifExistsReturnPlace(placeId);

        // 유저 확인
        Member findMember = memberDbService.ifExistsReturnMember(memberId);

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

        mbtiCountService.addMbtiCount(findMember, placeId);
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

        /*  Todo : 예약 가능한지 확인하는 이벤트 처리  */

        Long memberId = redisUtils.getId(refreshToken);

        // 사용자, 예약, 호스트 정보 조회
        Member findMember = memberDbService.ifExistsReturnMember(memberId);
        Reserve findReserve = reserveDbService.ifExistsReturnReserve(reserveId);
        Place findPlace = placeDbService.ifExistsReturnPlace(findReserve.getPlaceId());

        // 헤더에 정보 추가
        KakaoHeaders headers = feignService.setHeaders(KAKAO_AK + adminKey);
        readyToInfo(findMember, findReserve, findPlace);

        // 카카오 서버에 보내기 위한 params Map 생성
        ReadyToPaymentInfo params = setReadyParams(requestUrl, quantity, findPlace.getCharge());
        payReadyDto = feignService.getPaymentUrlResponse(headers, params);
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
    public PayApproveInfo getApprovedKaKaoPayInfo(String pgToken) {

        // 예약 정보 반환을 위한 headers, params 세팅
        KakaoHeaders headers = feignService.setHeaders(KAKAO_AK + adminKey);
        RequestForReserveInfo params = setRequestParams(pgToken);
        PayApproveInfo approvalDto = feignService.getSuccessResponse(headers, params);


        // 결제 성공시 예약 상태 변경 및 DTO에 메시지를 담는 과정
        String[] orderInfoList = parseOrderInfo(orderId);
        String reserveId = orderInfoList[1];
        approvalDto.setOrderStatus(ORDER_APPROVED);
        Reserve findReserve = reserveDbService.ifExistsReturnReserve(Long.valueOf(reserveId));
        findReserve.setStatus(Reserve.ReserveStatus.PAY_SUCCESS);
        reserveDbService.saveReserve(findReserve);

        return approvalDto;
    }


    /**
     * 예약 프로세스 4 - 결제 취소 시 결제 취소 상태 변경 메서드
     *
     * @author mozzi327
     */
    public void setCanceledStatus() {
        String reserveId = parseOrderInfo(orderId)[1];
        Reserve findReserve = reserveDbService.ifExistsReturnReserve(Long.valueOf(reserveId));
        findReserve.setStatus(Reserve.ReserveStatus.PAY_CANCELED);
        reserveDbService.saveReserve(findReserve);
    }


    /**
     * 예약 프로세스 5 - 결제 실패 시 결제 실패 상태 변경 메서드
     *
     * @author mozzi327
     */
    public void setFailedStatus() {
        String reserveId = parseOrderInfo(orderId)[1];
        Reserve findReserve = reserveDbService.ifExistsReturnReserve(Long.valueOf(reserveId));
        findReserve.setStatus(Reserve.ReserveStatus.PAY_FAILED);
        reserveDbService.saveReserve(findReserve);
    }


    /* ------------------------------------ 결제 외 메서드 -------------------------------------*/





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
     * 결과별 리다이렉트 Url 파라매터 입력 메서드(Feign Client)
     * @param requestUrl 요청 URL
     * @param quantity 총 예약 시간
     * @param charge 시간 당 금액
     * @return Params
     * @author mozzi327
     */
    private ReadyToPaymentInfo setReadyParams(String requestUrl, long quantity, long charge) {
        return ReadyToPaymentInfo.builder()
                .cid(testCid)
                .approval_url(requestUrl + approvalUri)
                .cancel_url(requestUrl + cancelUri)
                .fail_url(requestUrl + failUri)
                .partner_order_id(orderId)
                .partner_user_id(userId)
                .item_name(itemName)
                .quantity(String.valueOf(quantity))
                .total_amount(totalAmount.toString())
                .val_amount(String.valueOf(charge))
                .tax_free_amount(String.valueOf(taxFreeAmount))
                .build();
    }


    /**
     * 결제 진행을 위한 파라미터 세팅 메서드
     * @param findMember 조회 사용자 정보
     * @param findReserve 조회 진행 예약 정보
     * @param findPlace 조회 진행 장소 정보
     * @author mozzi327
     */
    private void readyToInfo(Member findMember,
                             Reserve findReserve,
                             Place findPlace) {
        orderId = findReserve.getId() + "/" + findMember.getId() + "/" + findPlace.getTitle();
        userId = findMember.getId().toString();
        itemName = findPlace.getTitle();
        totalAmount = findReserve.getTotalCharge();
        quantity = (findReserve.getEndTime().getHour() - findReserve.getStartTime().getHour());
    }


    /**
     * 결제 완료 후 예약 정보 조회를 위한 파라미터 세팅 메서드
     * @param pgToken Payment Gateway Token 정보
     * @return RequestForReserveInfo
     * @author mozzi327
     */
    private RequestForReserveInfo setRequestParams(String pgToken) {
        return RequestForReserveInfo.builder()
                .cid(testCid)
                .tid(payReadyDto.getTid())
                .partner_order_id(orderId)
                .partner_user_id(userId)
                .pg_token(pgToken)
                .total_amount(String.valueOf(totalAmount))
                .build();
    }


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

