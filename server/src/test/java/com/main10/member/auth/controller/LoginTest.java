package com.main10.member.auth.controller;

import com.main10.global.security.dto.LoginDto;
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
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class LoginTest extends AuthControllerTest {

    @Test
    @DisplayName("회원 로그인 테스트")
    public void loginMember() throws Exception {

        doNothing().when(memberDbService).saveMember(member);
        given(redisUtils.getData(Mockito.anyString(), Mockito.anyString())).willReturn(null);
        given(authService.loginMember(Mockito.any(LoginDto.class))).willReturn(resToken);

        String content = gson.toJson(login);

        ResultActions actions = this.mockMvc
                .perform(post("/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .with(csrf())
                );

        actions.andExpect(status().isOk())
                .andDo(document("회원 로그인",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestFields(
                                List.of(
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
                                )),
                        responseHeaders(
                                headerWithName(AUTHORIZATION).description("액세스 토큰"),
                                headerWithName(REFRESH).description("리프레시 토큰")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                        fieldWithPath("mbti").type(JsonFieldType.STRING).description("mbti")
                                )
                        )));
    }
}
