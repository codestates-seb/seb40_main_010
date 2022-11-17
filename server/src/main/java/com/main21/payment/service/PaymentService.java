package com.main21.payment.service;

import com.main21.payment.outbox.event.OutBoxEventBuilder;
import com.main21.payment.outbox.event.ReserveCreated;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

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

}
