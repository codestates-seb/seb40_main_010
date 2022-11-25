package com.main21.reserve.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PaymentValues {
    // kakaopay host URL 정보
    @Value("${kakao.host}")
    public static String HOST_VALUE;
    // kakaopay cid 정보(테스트용)
    @Value("${kakao.pay.cid}")
    public static String CID_VALUE;


    // 결제 처리 상태 리다이렉트 URI 정보
    @Value("${kakao.uri.fail}")
    public static String REDIRECT_FAIL_URI_VALUE;
    @Value("${kakao.uri.cancel}")
    public static String REDIRECT_CANCEL_URI_VALUE;
    @Value("${kakao.uri.approval}")
    public static String REDIRECT_SUCCESS_URI_VALUE;


    // 결제 정보 데이터 요청 URI 정보
    @Value("${kakao.pay.order}")
    public static String RESERVE_URI_VALUE;
    @Value("${kakao.pay.ready}")
    public static String PAYMENT_READY_URI_VALUE;
    @Value("${kakao.pay.cancel}")
    public static String CANCEL_REDIRECT_URI_VALUE;
    @Value("${kakao.pay.approve}")
    public static String PAYMENT_SUCCESS_URL;


    // 부가세 정보
    @Value("${kakao.pay.taxfree}")
    public static Integer TAX_FREE_AMOUNT_VALUE;


}
