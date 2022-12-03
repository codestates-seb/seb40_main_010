package com.main10.domain.reserve.feign;

import com.main10.domain.reserve.pay.PayApproveInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "response", url = "http://localhost:3000", configuration = {FeignErrorConfig.class})
public interface FeignClientResponse {

    /**
     * 카카오페이 결제 성공 시 발생하는 예약 정보 요청 인터페이스(콜백)
     * @return PayApprovalDto
     * @author mozzi327
     */
    @PostMapping(value = "/v1/payment/approve", consumes = "application/json")
    PayApproveInfo successForPayment(@RequestBody PayApproveInfo payApproveInfo);
}
