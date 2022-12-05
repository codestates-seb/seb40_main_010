package com.main10.member.member.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;
import java.util.List;

import static com.main10.utils.ApiDocumentUtils.getRequestPreProcessor;
import static com.main10.utils.ApiDocumentUtils.getResponsePreProcessor;
import static com.main10.utils.AuthConstants.AUTHORIZATION;
import static com.main10.utils.AuthConstants.REFRESH;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GetMemberTest extends MemberControllerTest {

    @Test
    @DisplayName("회원 정보 조회 테스트")
    public void getMember() throws Exception {
        given(memberService.getMember(Mockito.anyLong())).willReturn(info);

        ResultActions actions = mockMvc.perform(get("/member")
                .header(AUTHORIZATION, "Bearer " + accessToken)
                .header(REFRESH, refreshToken)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
        );

        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mbti").value(info.getMbti()))
                .andExpect(jsonPath("$.nickname").value(info.getNickname()))
                .andExpect(jsonPath("$.profileImage").value(info.getProfileImage()))
                .andDo(document("회원 정보 조회",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("액세스 토큰"),
                                headerWithName(REFRESH).description("리프레시 토큰")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("mbti").type(JsonFieldType.STRING).description("MBTI"),
                                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                                        fieldWithPath("profileImage").type(JsonFieldType.STRING).description("프로필이미지")
                                )
                        )));



    }

}
