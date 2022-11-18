package com.main21.reserve.service;

import com.main21.exception.BusinessLogicException;
import com.main21.exception.ExceptionCode;
import com.main21.member.entity.Member;
import com.main21.member.repository.MemberRepository;
import com.main21.place.entity.Place;
import com.main21.place.repository.PlaceRepository;
import com.main21.reserve.dto.PayApprovalDto;
import com.main21.reserve.dto.PayReadyDto;
import com.main21.reserve.dto.ReservationInfo;
import com.main21.reserve.entity.Reserve;
import com.main21.reserve.event.OutBoxEventBuilder;
import com.main21.reserve.event.ReserveCreated;
import com.main21.reserve.mapper.ReserveMapper;
import com.main21.reserve.repository.ReserveRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
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

import java.util.Arrays;

import static com.main21.reserve.utils.PayConstants.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

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
    private final MemberRepository memberRepository;
    private final ReserveRepository reserveRepository;
    private final PlaceRepository placeRepository;

    private PayReadyDto payReadyDto;
    private RestTemplate restTemplate;
    private String orderId;
    private String userId;
    private String itemName;
    private Long totalAmount;
    private boolean exceptionFlag = true;
    private final ReserveMapper reserveMapper;



    /**
     * 예약 프로세스 2 - 결제 Url 요청 메서드
     * @param reserveId 예약 식별자
     * @param memberId 사용자 식별자
     * @param requestUrl 요청 url
     * @return url(결제 페이지)
     * @author mozzi327
     */
    @Transactional
    public String getKaKaoPayUrl(Long reserveId,
                                 Long memberId,
                                 String requestUrl) {

         /*
            Todo : 예약 가능한지 확인하는 이벤트 처리

        */

        // 헤더에 정보 추가
        HttpHeaders headers = new HttpHeaders();
        setHeaders(headers);

        // 사용자, 예약, 호스트 정보 조회
        Member findMember = ifExistsReturnMember(memberId);
        Reserve findReserve = ifExistsReturnReserve(reserveId);
        Place findPlace = ifExistsReturnPlace(findReserve.getPlaceId());
        orderId = findReserve.getId() + "/" + findMember.getId() + "/" + findPlace.getTitle();
        userId = findMember.getId().toString();
        itemName = findPlace.getTitle();
        totalAmount = findReserve.getTotalCharge();
        long quantity = (findReserve.getEndTime().getTime() - findReserve.getStartTime().getTime()) / 3600000;

        // 카카오 서버에 보내기 위한 params Map 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        setKaKaoParams(params, requestUrl);
        params.add(PARTNER_ORDER_ID, orderId);
        params.add(PARTNER_USER_ID, userId);
        params.add(ITEM_NAME, itemName);
        params.add(QUANTITY, Long.toString(quantity));
        params.add(TOTAL_AMOUNT, totalAmount.toString());
        params.add(TAX_FREE_AMOUNT, String.valueOf(taxFreeAmount));

        /*
            Todo :

        */

        return getPayUrl(headers, params);
    }


    /**
     * 카카오페이 URL 생성 결과 리턴 메서드
     * @param headers http 헤더
     * @param params params Map
     * @return payReadyDto.getNextRedirectPcUrl() or null
     * @author mozzi327
     */
    private String getPayUrl(HttpHeaders headers,
                             MultiValueMap<String, String> params) {
        HttpEntity<MultiValueMap<String, String >> body = new HttpEntity<>(params, headers);

        try {
            payReadyDto = restTemplate.postForObject(host + kakaoPayReady,
                    body, PayReadyDto.class);

            return payReadyDto != null ? payReadyDto.getNextRedirectPcUrl() : null;
        } catch (RestClientException e) {
            log.error(e.getMessage());
        }
        return null;
    }


    /**
     * 예약 프로세스 3 - 결제 완료 시 결제 결과 반환 메서드
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
        Reserve findReserve = ifExistsReturnReserve(Long.valueOf(reserveId));
        findReserve.setStatus(Reserve.ReserveStatus.PAY_SUCCESS);
        reserveRepository.save(findReserve);

        try {
            return approvalDto;
        } catch (RestClientException e) {
            log.error(e.getMessage());
        }

        return null;
    }







    /* ------------------------------------ Common Method --------------------------------------*/


    /**
     * 결과별 리다이렉트 Url 파라매터 입력 메서드
     * @param params params Map
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
     * @param headers http 헤더
     * @author mozzi327
     */
    private void setHeaders(HttpHeaders headers) {
        restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        headers.add(AUTHORIZATION, KAKAO_AK + adminKey);
        headers.add(ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.add(CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE + UTF_8);
    }


    /**
     * OrderId를 파싱하는 메서드
     * @param orderId 카카오 주문 정보 식별자
     * @return String[]
     * @author mozzi327
     */
    private String[] parseOrderInfo(String orderId) {
        return orderId.split("/");
    }





/* ------------------------------------ find Method --------------------------------------*/

    /**
     * 예약 정보 조회 메서드
     * @param reserveId 예약 식별자
     * @return Reserve
     * @author mozzi327
     */
    private Reserve ifExistsReturnReserve(Long reserveId) {
        return reserveRepository.findById(reserveId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.RESERVATION_NOT_FOUND));
    }


    /**
     * 사용자 정보 조회 메서드
     * @param memberId 사용자 식별자
     * @return Member
     * @author mozzi327
     */
    private Member ifExistsReturnMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
    }


    /**
     * 호스팅 정보 조회 메서드
     * @param placeId 장소 식별자
     * @return Place
     * @author mozzi327
     */
    private Place ifExistsReturnPlace(Long placeId) {
        return placeRepository.findById(placeId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.PLACE_NOT_FOUND));
    }
}
