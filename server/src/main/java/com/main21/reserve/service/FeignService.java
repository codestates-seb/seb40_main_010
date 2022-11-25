package com.main21.reserve.service;

import com.main21.reserve.pay.PayApproveInfo;
import com.main21.reserve.pay.PayReadyInfo;
import com.main21.reserve.pay.RequestForReserveInfo;
import com.main21.reserve.feign.KaKaoFeignClient;
import com.main21.reserve.pay.KakaoHeaders;
import com.main21.reserve.pay.ReadyToPaymentInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import static com.main21.reserve.utils.PayConstants.UTF_8;

@Slf4j
@Service
public class FeignService {

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
    public KakaoHeaders setHeaders(String adminKey) {
        return KakaoHeaders.builder()
                .adminKey(adminKey)
                .accept(MediaType.APPLICATION_JSON + UTF_8)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE + UTF_8)
                .build();
    }
}
