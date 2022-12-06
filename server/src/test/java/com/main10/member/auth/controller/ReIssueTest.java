package com.main10.member.auth.controller;

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
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.BDDMockito.*;

public class ReIssueTest extends AuthControllerTest {

    @Test
    @DisplayName("액세스 토큰 리이슈 테스트")
    public void reIssueTokenTest() throws Exception {
        given(memberDbService.ifExistsReturnMember(Mockito.anyLong())).willReturn(member);
        given(redisUtils.getData(Mockito.anyString(), Mockito.anyString())).willReturn(null);
        given(authService.reIssueToken(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong())).willReturn(resToken);

        ResultActions actions = mockMvc.perform(get("/auth/re-issue")
                        .header(AUTHORIZATION, "Bearer " + accessToken)
                        .header(REFRESH, refreshToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf()));

        System.out.println(accessToken);

        actions.andExpect(status().isOk())
                .andDo(document("액세스 토큰 리이슈",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestHeaders(
                                List.of(
                                        headerWithName(AUTHORIZATION).description("액세스 토큰"),
                                        headerWithName(REFRESH).description("리프레시 토큰")
                                )),
                        responseHeaders(
                                headerWithName(AUTHORIZATION).description("리이슈 토큰"),
                                headerWithName(REFRESH).description("리프레시 토큰")
                        ), responseFields(
                                List.of(
                                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                        fieldWithPath("mbti").type(JsonFieldType.STRING).description("mbti")
                                )
                        )
                ));

    }
}
