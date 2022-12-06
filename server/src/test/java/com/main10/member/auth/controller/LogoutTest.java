package com.main10.member.auth.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import java.util.List;
import static com.main10.utils.ApiDocumentUtils.getRequestPreProcessor;
import static com.main10.utils.ApiDocumentUtils.getResponsePreProcessor;
import static com.main10.utils.AuthConstants.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LogoutTest extends AuthControllerTest {

    @Test
    @DisplayName("회원 로그아웃 테스트")
    public void logoutMember() throws Exception {

        doNothing().when(redisUtils).deleteData(Mockito.anyString(), Mockito.anyString());
        doNothing().when(redisUtils).setBlackList(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());
        doNothing().when(authService).logoutMember(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());

        ResultActions actions = mockMvc.perform(delete("/auth/logout")
                .header(AUTHORIZATION, "Bearer " + accessToken)
                .header(REFRESH, refreshToken)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(csrf()));

        actions.andExpect(status().isOk())
                .andDo(document("회원 로그아웃",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestHeaders(
                                List.of(
                                        headerWithName(AUTHORIZATION).description("액세스 토큰"),
                                        headerWithName(REFRESH).description("리프레시 토큰")
                                )
                        )));
    }
}
