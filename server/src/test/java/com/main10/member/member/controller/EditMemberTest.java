package com.main10.member.member.controller;

import com.main10.domain.member.dto.MemberDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import static com.main10.utils.ApiDocumentUtils.getRequestPreProcessor;
import static com.main10.utils.ApiDocumentUtils.getResponsePreProcessor;
import static com.main10.utils.AuthConstants.AUTHORIZATION;
import static com.main10.utils.AuthConstants.REFRESH;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class EditMemberTest extends MemberControllerTest {

    @Test
    @DisplayName("회원 정보 수정 테스트")
    public void patchMember() throws Exception {
        String content = gson.toJson(patch);
        doNothing().when(memberService).updateMember(Mockito.anyLong(), Mockito.any(MemberDto.Patch.class));


        ResultActions actions = mockMvc.perform(patch("/member/edit")
                .header(AUTHORIZATION, "Bearer " + accessToken)
                .header(REFRESH, refreshToken)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .content(content));

        actions
                .andExpect(status().isOk())
                .andDo(document("회원 정보 수정",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("액세스 토큰"),
                                headerWithName(REFRESH).description("리프레시 토큰")
                        ),
                        requestFields(
                                fieldWithPath("mbti").type(JsonFieldType.STRING).description("MBTI"),
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임")
                        )
                ));
    }
}
