package com.main21.controller;

import com.google.gson.Gson;
import com.main21.config.SecurityTestConfig;
import com.main21.member.controller.MemberController;
import com.main21.member.dto.MemberDto;
import com.main21.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static com.main21.utils.ApiDocumentUtils.getRequestPreProcessor;
import static com.main21.utils.ApiDocumentUtils.getResponsePreProcessor;


@WithMockUser
@WebMvcTest({MemberController.class, SecurityTestConfig.class})
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
public class MemberControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    @MockBean
    private MemberService memberService;

    @Test
    @DisplayName("회원가입 성공 테스트_201")
    public void 회원가입_성공_테스트() throws Exception {
        MemberDto.Post request = MemberDto.Post.builder()
                .email("hgd@gmail.com")
                .password("ghdrlfehd")
                .nickname("홍길동")
                .phoneNumber("010-1234-5678")
                .mbti("INFJ")
                .build();

        doNothing().when(memberService).createMember(request);

        String content = gson.toJson(request);

        ResultActions perform = mockMvc
                .perform(post("/member/join")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content(content));

        perform
                .andExpect(status().isCreated())
                .andDo(document(
                        "회원가입_성공",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestFields(
                                List.of(
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
                                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                                        fieldWithPath("phoneNumber").type(JsonFieldType.STRING).description("전화번호"),
                                        fieldWithPath("mbti").type(JsonFieldType.STRING).description("MBTI")
                                )
                        )
                ));
    }

}
