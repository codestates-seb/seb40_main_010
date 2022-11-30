package com.main10.member.member.controller;

import com.main10.domain.member.dto.MemberDto;
import org.junit.jupiter.api.DisplayName;

import static com.main10.utils.ApiDocumentUtils.getRequestPreProcessor;
import static com.main10.utils.ApiDocumentUtils.getResponsePreProcessor;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;


public class CreateMemberTest extends MemberControllerTest {

    @Test
    @DisplayName("회원가입 테스트")
    public void postMember() throws Exception {
        doNothing().when(memberService).createMember(Mockito.any(MemberDto.Post.class));
        String content = gson.toJson(post);

        ResultActions actions =
                mockMvc.perform(
                        post("/member/join")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .content(content)
                );

        actions.andExpect(status().isCreated())
                .andDo(document("일반 회원가입",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestFields(
                                List.of(
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
                                        fieldWithPath("mbti").type(JsonFieldType.STRING).description("MBTI"),
                                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                                        fieldWithPath("phoneNumber").type(JsonFieldType.STRING).description("전화번호")
                                )
                        )
                        ));
    }
}
