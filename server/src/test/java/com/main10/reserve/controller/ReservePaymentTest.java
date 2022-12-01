package com.main10.reserve.controller;

import com.main10.domain.reserve.dto.ReserveDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;

import static com.main10.utils.ApiDocumentUtils.getRequestPreProcessor;
import static com.main10.utils.AuthConstants.AUTHORIZATION;
import static com.main10.utils.AuthConstants.REFRESH;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ReservePaymentTest extends ReserveControllerTest{

    @Test
    @DisplayName("예약 프로세스 1 - 예약 등록")
    void postReserve() throws Exception {

        Long placeId = 1L;

        ReserveDto.Post post = ReserveDto.Post.builder()
                .startTime(LocalDateTime.of(2022, 11, 30, 15, 00))
                .endTime(LocalDateTime.of(2022, 11, 30, 16, 00))
                .capacity(2)
                .build();

        String content = gson.toJson(post);

        doNothing().when(reserveService)
                .createReserve(Mockito.any(ReserveDto.Post.class), Mockito.anyLong(), Mockito.anyString());

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
                        "예약 등록",
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
    void orderAction() {

    }

    @Test
    @DisplayName("예약 프로세스 3 - 결제 승인")
    void paySuccessAction() {

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
                        "결제 취소",
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
                        "결제 실패",
                        pathParameters(
                                parameterWithName("reserve-id").description("예약 식별자")
                        )
                ));
    }
}
