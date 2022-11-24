package com.main21.reserve.service;

import com.main21.exception.BusinessLogicException;
import com.main21.exception.ExceptionCode;
import com.main21.member.entity.Member;
import com.main21.member.service.MemberDbService;
import com.main21.place.entity.Place;
import com.main21.place.service.PlaceDbService;
import com.main21.reserve.dto.PayApprovalDto;
import com.main21.reserve.dto.PayReadyDto;
import com.main21.reserve.dto.ReserveDto;
import com.main21.reserve.entity.*;
import com.main21.reserve.event.OutBoxEventBuilder;
import com.main21.reserve.event.ReserveCreated;
import com.main21.reserve.feign.KaKaoFeignClient;
import com.main21.security.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.text.ParseException;


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

    private final ApplicationEventPublisher applicationEventPublisher;
    private final OutBoxEventBuilder<ReserveCreated> outBoxEventBuilder;
    private KaKaoFeignClient kaKaoFeignClient;

    private final RedisUtils redisUtils;
    private final ReserveDbService reserveDbService;
    private final MemberDbService memberDbService;
    private final MbtiCountService mbtiCountService;

    private final PlaceDbService placeDbService;

    private PayReadyDto payReadyDto;
    private RestTemplate restTemplate;
    private String orderId;
    private String userId;
    private String itemName;
    private Long totalAmount;
    private boolean exceptionFlag = true;

    /**
     * 예약 프로세스 1 - 예약 등록 메서드
     *
     * @param post     예약 등록 정보
     * @param placeId  장소 식별자
     * @param refreshToken
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
                .totalCharge((long) (((post.getEndTime().getHour() - post.getStartTime().getHour())) * findPlace.getCharge()))
                .build();


        // 최대 수용인원 초과 예약 금지
        if (reserve.getCapacity() > findPlace.getMaxCapacity()) {
            throw new BusinessLogicException(ExceptionCode.RESERVATION_MAX_CAPACITY_OVER);
        } else reserveDbService.saveReserve(reserve);

        mbtiCountService.addMbtiCount(findMember, placeId);
    }


    /**
     * 예약 프로세스 2 - 결제 URL 요청 메서드
     *
     * @param reserveId  예약 식별자
     * @param refreshToken
     * @param requestUrl 요청 URL
     * @return URL(결제 페이지)
     * @author mozzi327
     */
    @Transactional
    public String getKaKaoPayUrl(Long reserveId,
                                 String refreshToken,
                                 String requestUrl) {

        /*
            Todo : 예약 가능한지 확인하는 이벤트 처리
        */

        Long memberId = redisUtils.getId(refreshToken);

        // 헤더에 정보 추가
        HttpHeaders headers = new HttpHeaders();

        setHeaders(headers);

        // 사용자, 예약, 호스트 정보 조회
        Member findMember = memberDbService.ifExistsReturnMember(memberId);
        Reserve findReserve = reserveDbService.ifExistsReturnReserve(reserveId);
        Place findPlace = placeDbService.ifExistsReturnPlace(findReserve.getPlaceId());
        orderId = findReserve.getId() + "/" + findMember.getId() + "/" + findPlace.getTitle();
        userId = findMember.getId().toString();
        itemName = findPlace.getTitle();
        totalAmount = findReserve.getTotalCharge();
        long quantity = (findReserve.getEndTime().getHour() - findReserve.getStartTime().getHour());

        // 카카오 서버에 보내기 위한 params Map 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        setKaKaoParams(params, requestUrl);
        params.add(PARTNER_ORDER_ID, orderId);
        params.add(PARTNER_USER_ID, userId);
        params.add(ITEM_NAME, itemName);
        params.add(QUANTITY, String.valueOf(quantity));
        params.add(TOTAL_AMOUNT, totalAmount.toString());
        params.add(VAL_AMOUNT, String.valueOf(findPlace.getCharge()));
        params.add(TAX_FREE_AMOUNT, String.valueOf(taxFreeAmount));

        /*
            Todo :

        */

        return getPayUrl(headers, params);
    }


    /**
     * 예약 프로세스 3 - 결제 완료 시 결제 결과 반환 메서드
     *
     * @param pgToken Payment Gateway Token
     * @return PayApprovalDto
     * @author mozzi327
     */
    @Transactional
    public PayApprovalDto getApprovedKaKaoPayInfo(String pgToken) {
        HttpHeaders headers = new HttpHeaders();
        setHeaders(headers);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(CID, testCid);
        params.add(TID, payReadyDto.getTid());
        params.add(PARTNER_ORDER_ID, orderId);
        params.add(PARTNER_USER_ID, userId);
        params.add(PG_TOKEN, pgToken);
        params.add(TOTAL_AMOUNT, String.valueOf(totalAmount));

        HttpEntity<MultiValueMap<String, String>> body = new HttpEntity<>(params, headers);

        PayApprovalDto approvalDto = restTemplate
                .postForObject(host + kakaoPayApprove, body, PayApprovalDto.class);

        // 결제 실패 시 발생
        if (approvalDto == null) return null;

        // 예약 상태 변경 및 DTO에 메시지를 담는 과정
        String[] orderInfoList = parseOrderInfo(orderId);
        String reserveId = orderInfoList[1];

        approvalDto.setOrderStatus(ORDER_APPROVED);
        Reserve findReserve = reserveDbService.ifExistsReturnReserve(Long.valueOf(reserveId));
        findReserve.setStatus(Reserve.ReserveStatus.PAY_SUCCESS);
        reserveDbService.saveReserve(findReserve);

        try {
            return approvalDto;
        } catch (RestClientException e) {
            log.error(e.getMessage());
        }

        return null;
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


    /**
     * 카카오페이 URL 생성 결과 리턴 메서드
     *
     * @param headers http 헤더
     * @param params  params Map
     * @return payReadyDto.getNextRedirectPcUrl() or null
     * @author mozzi327
     */
    private String getPayUrl(MultiValueMap<String, String> headers,
                             MultiValueMap<String, String> params) {

        HttpEntity<MultiValueMap<String, String>> body = new HttpEntity<>(params, headers);

        try {
//            payReadyDto = kaKaoFeignClient.readyForPayment(
//                    params.get(CID).toString(),
//                    params.get(PARTNER_ORDER_ID).toString(),
//                    params.get(PARTNER_USER_ID).toString(),
//                    params.get(ITEM_NAME).toString(),
//                    params.get(QUANTITY).toString(),
//                    params.get(TOTAL_AMOUNT).toString(),
//                    params.get(VAL_AMOUNT).toString(),
//                    params.get(TAX_FREE_AMOUNT).toString(),
//                    params.get(APPROVAL_URL).toString(),
//                    params.get(FAIL_URL).toString(),
//                    params.get(CANCEL_URL).toString());

            payReadyDto = restTemplate.postForObject(host + kakaoPayReady,
                    body, PayReadyDto.class);

            return payReadyDto != null ? payReadyDto.getNextRedirectPcUrl() : null;
        } catch (RestClientException e) {
            log.error(e.getMessage());
        }
        return null;
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
     * @param reserveId 예약 식별자
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
     * @param patch     에약 수정 정보
     * @param reserveId 예약 식별자
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
     * 결과별 리다이렉트 Url 파라매터 입력 메서드
     *
     * @param params     params Map
     * @param requestUrl 요청하는 url(localhost)
     * @author mozzi327
     */
    private void setKaKaoParams(MultiValueMap<String, String> params,
                                String requestUrl) {
        params.add(CID, testCid);
        params.add(APPROVAL_URL, requestUrl + approvalUri);
        params.add(CANCEL_URL, requestUrl + cancelUri);
        params.add(FAIL_URL, requestUrl + failUri);
    }


    /**
     * 카카오페이 통신 헤더 세팅 메서드
     *
     * @param headers http 헤더
     * @author mozzi327
     */
    private void setHeaders(HttpHeaders headers) {
        restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        headers.add(ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.add(AUTHORIZATION, KAKAO_AK + adminKey);
        headers.add(CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE + UTF_8);

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

