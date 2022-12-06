package com.main10.reserve.controller;

import com.main10.domain.reserve.dto.ReserveDto;
import com.main10.domain.reserve.pay.Amount;
import com.main10.domain.reserve.pay.CardInfo;
import com.main10.domain.reserve.pay.PayApproveInfo;
import com.main10.domain.reserve.pay.PayReadyInfo;
import com.main10.domain.reserve.response.Message;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static com.main10.domain.reserve.utils.ReserveConstants.INFO_URI_MSG;
import static com.main10.domain.reserve.utils.ReserveConstants.PAY_URI_MSG;
import static com.main10.utils.ApiDocumentUtils.getRequestPreProcessor;
import static com.main10.utils.AuthConstants.AUTHORIZATION;
import static com.main10.utils.AuthConstants.REFRESH;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ReservePaymentTest extends ReserveControllerTest{

    @Test
    @DisplayName("예약 프로세스 1 - 예약 등록")
    void postReserve() throws Exception {

        Long placeId = 1L;

        ReserveDto.Post post = ReserveDto.Post.builder()
                .startTime(LocalDateTime.of(2022, 11, 30, 15, 0))
                .endTime(LocalDateTime.of(2022, 11, 30, 16, 0))
                .capacity(2)
                .build();

        String content = gson.toJson(post);

        given(reserveService.createReserve(Mockito.any(ReserveDto.Post.class), Mockito.anyLong(), Mockito.anyLong())).willReturn(1L);

        ResultActions actions =
                mockMvc.perform(
                        post("/place/{place-id}/reserve", placeId)
                                .header(AUTHORIZATION, "Bearer " + accessToken)
                                .header(REFRESH, refreshToken)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                                .with(csrf())
                );

        actions
                .andExpect(status().isCreated())
                .andDo(document(
                        "예약1 예약 등록",
                        getRequestPreProcessor(),
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("엑세스 토큰"),
                                headerWithName(REFRESH).description("리프레시 토큰")
                        ),pathParameters(
                                parameterWithName("place-id").description("장소 식별자")
                        ),
                        requestFields(
                                List.of(
                                        fieldWithPath("startTime").type(JsonFieldType.STRING).description("예약 시작 시간"),
                                        fieldWithPath("endTime").type(JsonFieldType.STRING).description("예약 종료 시간"),
                                        fieldWithPath("capacity").type(JsonFieldType.NUMBER).description("예약 인원")
                                )
                        )
                ));
    }

    @Test
    @DisplayName("예약 프로세스 2 - 사용자 결제 화면 전송")
    void orderAction() throws Exception {

        PayReadyInfo payReadyDto = PayReadyInfo.builder()
                .nextRedirectPcUrl("nextRedirectPcUrl")
                .build();

        Message<Object> message = Message.builder()
                .data(payReadyDto.getNextRedirectPcUrl())
                .message(PAY_URI_MSG)
                .build();

        given(reserveService.getKaKaoPayUrl(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyString()))
                .willReturn(message);

        ResultActions actions =
                mockMvc.perform(
                        get("/place/reserve/{reserve-id}/payment", reserve.getId())
                                .header(AUTHORIZATION, "Bearer " + accessToken)
                                .header(REFRESH, refreshToken)
                );

        actions
                .andExpect(status().isOk())
                .andDo(document(
                        "예약2 사용자 결제 화면 전송",
                        getRequestPreProcessor(),
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("엑세스 토큰"),
                                headerWithName(REFRESH).description("리프레시 토큰")
                        ),
                        pathParameters(
                                parameterWithName("reserve-id").description("장소 식별자")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.STRING).description("결제 URL"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("메세지")
                                )
                        )
                ));

    }

    @Test
    @DisplayName("예약 프로세스 3 - 결제 승인")
    void paySuccessAction() throws Exception {

        String pgToken = "pgToken";

        Date date = new Date(2022, 12, 1, 20, 0);

        CardInfo cardInfo = CardInfo.builder()
                .purchaseCorp("purchaseCorp")
                .purchaseCorpCode("purchaseCorpCode")
                .issuerCorp("issuerCorp")
                .issuerCorpCode("issuerCorpCode")
                .bin("bin")
                .cardType("cardType")
                .installMonth("installMonth")
                .approvedId("approvedId")
                .cardMid("cardMid")
                .interestFreeInstall("interestFreeInstall")
                .cardItemCode("cardItemCode")
                .build();

        Amount amount = Amount.builder()
                .total(5)
                .taxFree(5)
                .vat(5)
                .point(5)
                .discount(5)
                .build();

        PayApproveInfo payInfo = PayApproveInfo.builder()
                .aid("aid")
                .tid("tid")
                .cid("cid")
                .sid("sid")
                .partnerOrderId("partnerOrderId")
                .partnerUserId("partnerUserId")
                .paymentMethodType("paymentMethodType")
                .amount(amount)
                .cardInfo(cardInfo)
                .itemName("itemName")
                .itemCode("itemCode")
                .payload("payload")
                .quantity(3)
                .taxFreeAmount(5)
                .vatAmount(3)
                .createdAt(date)
                .approvedAt(date)
                .orderStatus("orderStatus")
                .build();

        Message message = Message.builder()
                .data(payInfo)
                .message(INFO_URI_MSG)
                .build();

        given(reserveService.getApprovedKaKaoPayInfo(Mockito.anyLong(), Mockito.anyString()))
                .willReturn(message);

        ResultActions actions =
                mockMvc.perform(
                        get("/api/reserve/{reserve-id}/completed", reserve.getId())
                                .header(AUTHORIZATION, "Bearer " + accessToken)
                                .header(REFRESH, refreshToken)
                                .param("pg_token", pgToken));

        actions
                .andExpect(status().isOk())
                .andDo(document(
                        "예약3 결제 승인",
                        getRequestPreProcessor(),
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("엑세스 토큰"),
                                headerWithName(REFRESH).description("리프레시 토큰")
                        ),
                        pathParameters(
                                parameterWithName("reserve-id").description("장소 식별자")
                        ),
                        requestParameters(
                                parameterWithName("pg_token").description("pgToken")
                        )
//                        responseFields(
//                                List.of(
//                                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("결제 데이터").optional(),
//                                        fieldWithPath("data.aid").type(JsonFieldType.STRING).description("aid"),
//                                        fieldWithPath("data.tid").type(JsonFieldType.STRING).description("tid"),
//                                        fieldWithPath("data.cid").type(JsonFieldType.STRING).description("cid"),
//                                        fieldWithPath("data.sid").type(JsonFieldType.STRING).description("sid"),
//                                        fieldWithPath("data.partner_order_id").type(JsonFieldType.STRING).description("partnerOrderId"),
//                                        fieldWithPath("data.partner_user_id").type(JsonFieldType.STRING).description("partnerUserId"),
//                                        fieldWithPath("data.payment_method_type").type(JsonFieldType.STRING).description("paymentMethodType"),
//
//                                        fieldWithPath("data.amount").type(JsonFieldType.OBJECT).description("amount 데이터"),
//                                        fieldWithPath("data.amount.total").type(JsonFieldType.NUMBER).description("total"),
//                                        fieldWithPath("data.amount.tax_free").type(JsonFieldType.NUMBER).description("taxFree"),
//                                        fieldWithPath("data.amount.vat").type(JsonFieldType.NUMBER).description("vat"),
//                                        fieldWithPath("data.amount.point").type(JsonFieldType.NUMBER).description("point"),
//                                        fieldWithPath("data.amount.discount").type(JsonFieldType.NUMBER).description("discount"),
//
//                                        fieldWithPath("data.card_info").type(JsonFieldType.OBJECT).description("cardInfo 데이터").optional(),
//                                        fieldWithPath("data.card_info.purchase_corp").type(JsonFieldType.STRING).description("purchaseCorp"),
//                                        fieldWithPath("data.card_info.purchase_corp_code").type(JsonFieldType.STRING).description("purchaseCorpCode"),
//                                        fieldWithPath("data.card_info.issuer_corp").type(JsonFieldType.STRING).description("issuerCorp"),
//                                        fieldWithPath("data.card_info.issuer_corp_code").type(JsonFieldType.STRING).description("issuerCorpCode"),
//                                        fieldWithPath("data.card_info.bin").type(JsonFieldType.STRING).description("bin"),
//                                        fieldWithPath("data.card_info.card_type").type(JsonFieldType.STRING).description("cardType"),
//                                        fieldWithPath("data.card_info.install_month").type(JsonFieldType.STRING).description("installMonth"),
//                                        fieldWithPath("data.card_info.approved_id").type(JsonFieldType.STRING).description("approvedId"),
//                                        fieldWithPath("data.card_info.card_mid").type(JsonFieldType.STRING).description("cardMid"),
//                                        fieldWithPath("data.card_info.interest_free_install").type(JsonFieldType.STRING).description("interestFreeInstall"),
//                                        fieldWithPath("data.card_info.card_item_code").type(JsonFieldType.STRING).description("cardItemCode"),
//
//                                        fieldWithPath("data.item_name").type(JsonFieldType.STRING).description("itemName"),
//                                        fieldWithPath("data.item_code").type(JsonFieldType.STRING).description("itemCode"),
//                                        fieldWithPath("data.payload").type(JsonFieldType.STRING).description("payload"),
//                                        fieldWithPath("data.quantity").type(JsonFieldType.NUMBER).description("quantity"),
//                                        fieldWithPath("data.tax_free_amount").type(JsonFieldType.NUMBER).description("taxFreeAmount"),
//                                        fieldWithPath("data.vat_amount").type(JsonFieldType.NUMBER).description("vatAmount"),
//                                        fieldWithPath("data.created_at").type(JsonFieldType.STRING).description("createdAt"),
//                                        fieldWithPath("data.approved_at").type(JsonFieldType.STRING).description("approvedAt"),
//                                        fieldWithPath("data.order_status").type(JsonFieldType.STRING).description("orderStatus"),
//
//                                        fieldWithPath("message").type(JsonFieldType.STRING).description("메세지")
//                                )
//                        )
                ));
    }

    @Test
    @DisplayName("예약 프로세스 4 - 결제 취소")
    void payCancelAction() throws Exception{

        doNothing().when(reserveService).setFailedStatus(reserve.getId());
        ResultActions actions =
                mockMvc.perform(
                        get("/api/reserve/{reserve-id}/cancel", reserve.getId())
                );

        actions
                .andExpect(status().isOk())
                .andDo(document(
                        "예약4 결제 취소",
                        pathParameters(
                                parameterWithName("reserve-id").description("예약 식별자")
                        )
                ));

    }

    @Test
    @DisplayName("예약 프로세스 5 - 결제 실패")
    void payFailedAction() throws Exception {

        doNothing().when(reserveService).setFailedStatus(reserve.getId());
        ResultActions actions =
                mockMvc.perform(
                        get("/api/reserve/{reserve-id}/fail", reserve.getId())
                );

        actions
                .andExpect(status().isOk())
                .andDo(document(
                        "예약5 결제 실패",
                        pathParameters(
                                parameterWithName("reserve-id").description("예약 식별자")
                        )
                ));
    }
}
