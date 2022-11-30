package com.main10.domain.reserve.feign;

import com.main10.domain.reserve.pay.PayApproveInfo;
import com.main10.domain.reserve.pay.PayReadyInfo;
import com.main10.domain.reserve.pay.ReadyToPaymentInfo;
import com.main10.domain.reserve.pay.RequestForReserveInfo;
import com.main10.domain.reserve.utils.PayConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

/**
 * feign client 카카오페이 api 통신 인터페이스
 * @author mozzi327
 */
@FeignClient(value = "kakaopay", url = "https://kapi.kakao.com", configuration = {FeignErrorConfig.class})
public interface KaKaoFeignClient {

    /**
     * 카카오페이 결제 URL 요청 인터페이스
     * @param accept Accept Header
     * @param contentType Content-Type Header
     * @param query Query Param 정보
     * @return PayReadyDto
     * @author mozzu327
     */
    @PostMapping(value = "/v1/payment/ready")
    PayReadyInfo readyForPayment(
            @RequestHeader(PayConstants.AUTHORIZATION) String authorization,
            @RequestHeader(PayConstants.ACCEPT) String accept,
            @RequestHeader(PayConstants.CONTENT_TYPE) String contentType,
            @SpringQueryMap ReadyToPaymentInfo query);


    /**
     * 카카오페이 결제 성공 시 발생하는 예약 정보 요청 인터페이스
     * @param accept Accept Header
     * @param contentType Content-Type Header
     * @param query Query Param 정보
     * @return PayApprovalDto
     * @author mozzi327
     */
    @PostMapping(value = "/v1/payment/approve")
    PayApproveInfo successForPayment(
            @RequestHeader(PayConstants.AUTHORIZATION) String authorization,
            @RequestHeader(PayConstants.ACCEPT) String accept,
            @RequestHeader(PayConstants.CONTENT_TYPE) String contentType,
            @SpringQueryMap RequestForReserveInfo query);
}
