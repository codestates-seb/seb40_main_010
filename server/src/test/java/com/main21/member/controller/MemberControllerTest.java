package com.main21.member.controller;

import com.google.gson.Gson;
import com.main21.member.dto.MemberDto;
import com.main21.member.service.MemberService;
import com.main21.security.utils.JwtTokenUtils;
import com.main21.security.utils.RedisUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static com.main21.utils.ApiDocumentUtils.getRequestPreProcessor;
import static com.main21.utils.ApiDocumentUtils.getResponsePreProcessor;
import static com.main21.utils.AuthConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.mockito.BDDMockito.*;


@AutoConfigureMockMvc
@AutoConfigureRestDocs
@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(controllers = {MemberController.class})
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    @MockBean
    private MemberService memberService;

    @MockBean
    private RedisUtils redisUtils;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @BeforeEach
    void setUp() throws Exception {
        jwtTokenUtils = new JwtTokenUtils(SECRET_KEY, ACCESS_EXIRATION_MINUTE, REFRESH_EXIRATION_MINUTE);
        MemberDto.Post request = MemberDto.Post.builder()
                .email("hgd@gmail.com")
                .password("ghdrlfehd")
                .nickname("홍길동")
                .phoneNumber("010-1234-5678")
                .mbti("INFJ")
                .build();

//        given(redisUtils.getId(Mockito.anyString())).willReturn(1L);
        doNothing().when(memberService).createMember(request);

        String content = gson.toJson(request);

        ResultActions perform = mockMvc
                .perform(post("/member/join")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content(content));

//        perform
//                .andExpect(status().isCreated())
//                .andDo(document(
//                        "회원가입 성공",
//                        getRequestPreProcessor(),
//                        getResponsePreProcessor(),
//                        requestFields(
//                                List.of(
//                                        field
//                                )
//                        )
//                ))
    }

    @Test
    @WithMockUser
    void postMember() {

    }

    @Test
    void patchMember() {
    }

    @Test
    void getMember() {
    }

    @Test
    void createMemberImageS3() {
    }

    @Test
    void createMemberImage() {
    }
}