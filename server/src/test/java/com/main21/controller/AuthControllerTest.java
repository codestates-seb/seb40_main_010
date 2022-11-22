package com.main21.controller;

import com.google.gson.Gson;
import com.main21.member.dto.MemberDto;
import com.main21.member.entity.Member;
import com.main21.member.repository.MemberRepository;
import com.main21.member.service.MemberService;
import com.main21.security.dto.LoginDto;
import com.main21.security.utils.JwtTokenUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static com.main21.utils.ApiDocumentUtils.getRequestPreProcessor;
import static com.main21.utils.ApiDocumentUtils.getResponsePreProcessor;
import static com.main21.utils.AuthConstants.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    @MockBean
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    private MemberDto.Post request;

    private JwtTokenUtils jwtTokenUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @BeforeEach
    void setUp() {
        jwtTokenUtils = new JwtTokenUtils(
                SECRET_KEY,
                ACCESS_EXIRATION_MINUTE,
                REFRESH_EXIRATION_MINUTE);

        request = MemberDto.Post.builder()
                .email("hgd@gmail.com")
                .password("ghdrlfehd")
                .nickname("홍길동")
                .phoneNumber("010-1234-5678")
                .mbti("INFJ")
                .build();
//        doNothing().when(memberService).createMember(request);

        Member newMember = Member.builder()
                .email("hgd@gmail.com")
                .password(passwordEncoder.encode("ghdrlfehd"))
                .nickname("홍길동")
                .phoneNumber("010-1234-5678")
                .roles(List.of("USER"))
                .mbti("INFJ")
                .build();

        memberRepository.save(newMember);
    }

    @Test
    @DisplayName("로그인 테스트 200")
    public void 로그인_테스트_200() throws Exception {
        LoginDto login = LoginDto
                .builder()
                .email("hgd@gmail.com")
                .password("ghdrlfehd")
                .build();

        String content = gson.toJson(login);

        ResultActions perform = mockMvc
                .perform(post("/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content(content));

        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.nickname").value("홍길동"))
                .andExpect(jsonPath("$.email").value("hgd@gmail.com"))
                .andDo(document(
                        "로그인_성공",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestFields(
                                List.of(
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
                                )
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임")
                                )
                        )
                ));
    }
}
