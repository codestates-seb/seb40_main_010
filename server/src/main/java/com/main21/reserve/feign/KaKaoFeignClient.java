package com.main21.reserve.feign;

import com.main21.reserve.dto.PayReadyDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import static com.main21.reserve.utils.PayConstants.*;

@FeignClient(name = "feign", url = "https://kapi.kakao.com/v1/payment/ready", configuration = FeignConfig.class)
public interface KaKaoFeignClient {

    @PostMapping
    PayReadyDto readyForPayment(@RequestParam(name = CID) String testCid,
                                @RequestParam(name = PARTNER_ORDER_ID) String orderId,
                                @RequestParam(name = PARTNER_USER_ID) String userId,
                                @RequestParam(name = ITEM_NAME) String itemName,
                                @RequestParam(name = QUANTITY) String quantity,
                                @RequestParam(name = TOTAL_AMOUNT) String totalAmount,
                                @RequestParam(name = VAL_AMOUNT) String valAmount,
                                @RequestParam(name = TAX_FREE_AMOUNT) String taxFreeAmount,
                                @RequestParam(name = APPROVAL_URL) String approvalUrl,
                                @RequestParam(name = FAIL_URL) String failUrl,
                                @RequestParam(name = CANCEL_URL) String cancelUrl);
}
