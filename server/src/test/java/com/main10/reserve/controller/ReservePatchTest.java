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

import static com.main10.domain.member.utils.AuthConstant.AUTHORIZATION;
import static com.main10.utils.ApiDocumentUtils.getRequestPreProcessor;
import static com.main10.utils.ApiDocumentUtils.getResponsePreProcessor;
import static com.main10.utils.AuthConstants.REFRESH;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ReservePatchTest extends ReserveControllerTest {

    @Test
    @DisplayName("PATCH 예약 수정")
    void patchReserve() throws Exception {
        Long reserveId = 1L;

        ReserveDto.Patch patch = ReserveDto.Patch.builder()
                .capacity(5)
                .startTime(LocalDateTime.of(2022, 11, 30, 15, 00))
                .endTime(LocalDateTime.of(2022, 11, 30, 16, 00))
                .build();

        String content = gson.toJson(patch);
        doNothing().when(reserveService).updateReserve(Mockito.any(ReserveDto.Patch.class), Mockito.anyLong(), Mockito.anyLong());

        ResultActions actions =
                mockMvc.perform(
                        patch("/place/reserve/{reserve-id}/edit", reserveId)
                                .header(AUTHORIZATION, "Bearer " + accessToken)
                                .header(REFRESH, refreshToken)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                                .with(csrf())
                );

        actions
                .andExpect(status().isOk())
                .andDo(document("예약 수정",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),

                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("엑세스 토큰"),
                                headerWithName(REFRESH).description("리프레시 토큰")
                        ),
                        pathParameters(
                                parameterWithName("reserve-id").description("예약 식별자")
                        ),
                        requestFields(List.of(
                                        fieldWithPath("capacity").type(JsonFieldType.NUMBER).description("수용 인원"),
                                        fieldWithPath("startTime").type(JsonFieldType.STRING).description("시작 시간"),
                                        fieldWithPath("endTime").type(JsonFieldType.STRING).description("끝 시간")
                                )
                        )
                ));
    }
}
