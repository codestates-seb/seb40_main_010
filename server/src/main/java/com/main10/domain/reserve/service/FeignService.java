package com.main10.domain.reserve.service;

import com.main10.domain.member.entity.Member;
import com.main10.domain.place.entity.Place;
import com.main10.domain.reserve.pay.KakaoHeaders;
import com.main10.domain.reserve.pay.PayApproveInfo;
import com.main10.domain.reserve.pay.ReadyToPaymentInfo;
import com.main10.domain.reserve.utils.PayConstants;
import com.main10.domain.reserve.entity.Reserve;
import com.main10.domain.reserve.pay.PayReadyInfo;
import com.main10.domain.reserve.pay.RequestForReserveInfo;
import com.main10.domain.reserve.feign.KaKaoFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;

/**
 * feign client를 통한 카카오페이 api 통신 서비스 클래스
 * @author mozzi327
 */
@Slf4j
@Service
@Transactional
public class FeignService {

    @Value("${kakao.admin.key}")
    private String adminKey;

    @Value("${kakao.uri.pay-process}")
    private String paymentProcessUri;

    @Value("${kakao.pay.cid}")
    private String testCid;

    @Value("${kakao.pay.taxfree}")
    private Integer taxFreeAmount;


    @Autowired
    KaKaoFeignClient kaKaoFeignClient;


    /**
     * 카카오페이 URL 생성 결과 리턴 메서드
     *
     * @param headers http 헤더
     * @param params  paramsInfo
     * @return payReadyDto or null
     * @author mozzi327
     */
    public PayReadyInfo getPaymentUrlResponse(KakaoHeaders headers,
                                              ReadyToPaymentInfo params) {
        try {
            return kaKaoFeignClient
                    .readyForPayment(
                            headers.getAdminKey(),
                            headers.getAccept(),
                            headers.getContentType(),
                            params);
        } catch (RestClientException e) {
            log.error(e.getMessage());
        }
        return null;
    }


    /**
     * 카카오페이 결제 완료 후 예약 정보 요청 메서드
     * @param headers http 헤더
     * @param params paramsInfo
     * @return PayApprovalDto or null
     * @author mozzu327
     */
    public PayApproveInfo getSuccessResponse(KakaoHeaders headers,
                                             RequestForReserveInfo params) {
        try {
            return kaKaoFeignClient
                    .successForPayment(
                            headers.getAdminKey(),
                            headers.getAccept(),
                            headers.getContentType(),
                            params);
        } catch (RestClientException e) {
            log.error(e.getMessage());
        }
        return null;
    }


    /**
     * 카카오페이 결제를 위해 필요한 필수 헤더 세팅 메서드
     * @return KakaoHeaders
     * @author mozzi327
     */
    public KakaoHeaders setHeaders() {
        return KakaoHeaders.builder()
                .adminKey(PayConstants.KAKAO_AK + adminKey)
                .accept(MediaType.APPLICATION_JSON + PayConstants.UTF_8)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE + PayConstants.UTF_8)
                .build();
    }


    /**
     * 결과별 리다이렉트 Url 파라매터 입력 메서드(Feign Client)
     * @param requestUrl 요청 URL
     * @param findMember 사용자 정보
     * @param findReserve 예약 정보
     * @param findPlace 장소 정보
     * @return ReadyToPaymentInfo
     * @author mozzi327
     */
    public ReadyToPaymentInfo setReadyParams(String requestUrl, Member findMember, Reserve findReserve, Place findPlace) {
        Long reserveId = findReserve.getId();
        String orderId = findReserve.getId() + "/" + findMember.getId() + "/" + findPlace.getTitle();
        String userId = findMember.getId().toString();
        String itemName = findPlace.getTitle();
        String totalAmount = findReserve.getTotalCharge().toString();
        Long quantity = ((long)findReserve.getEndTime().getHour() - findReserve.getStartTime().getHour());

        return ReadyToPaymentInfo.builder()
                .cid(testCid)
                .approval_url(requestUrl + paymentProcessUri + "/" + reserveId + "/completed")
                .cancel_url(requestUrl + paymentProcessUri + "/" + reserveId + "/cancel")
                .fail_url(requestUrl + paymentProcessUri + "/" + reserveId + "/fail")
                .partner_order_id(orderId)
                .partner_user_id(userId)
                .item_name(itemName)
                .quantity(String.valueOf(quantity))
                .total_amount(totalAmount)
                .val_amount(String.valueOf(findPlace.getCharge()))
                .tax_free_amount(String.valueOf(taxFreeAmount))
                .build();
    }


    /**
     * 결제 완료 후 예약 정보 조회를 위한 파라미터 세팅 메서드
     * @param pgToken Payment Gateway Token 정보
     * @return RequestForReserveInfo
     * @author mozzi327
     */
    public RequestForReserveInfo setRequestParams(String pgToken, Reserve findReserve) {
        return RequestForReserveInfo.builder()
                .cid(findReserve.getCid())
                .tid(findReserve.getTid())
                .partner_order_id(findReserve.getPartnerOrderId())
                .partner_user_id(findReserve.getPartnerUserId())
                .pg_token(pgToken)
                .total_amount(findReserve.getTotalAmount())
                .build();
    }
}
