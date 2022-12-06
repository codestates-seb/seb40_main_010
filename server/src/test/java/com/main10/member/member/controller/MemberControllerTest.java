package com.main10.member.member.controller;

import com.google.gson.Gson;
import com.main10.domain.member.dto.MemberDto;
import com.main10.domain.member.entity.Member;
import com.main10.domain.member.controller.MemberController;
import com.main10.domain.member.service.MemberDbService;
import com.main10.domain.member.service.MemberService;
import com.main10.global.security.token.JwtAuthenticationToken;
import com.main10.global.security.utils.JwtTokenUtils;
import com.main10.global.security.utils.RedisUtils;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import java.util.stream.Collectors;

import static com.main10.utils.AuthConstants.*;

@WithMockUser
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@WebMvcTest({MemberController.class})
@MockBean(JpaMetamodelMappingContext.class)
public class MemberControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected Gson gson;

    @MockBean
    protected MemberService memberService;

    @MockBean
    protected MemberDbService memberDbService;

    @MockBean
    protected RedisUtils redisUtils;

    protected JwtTokenUtils jwtTokenUtils;


    protected Member member;
    protected MemberDto.Post post;
    protected MemberDto.Patch patch;
    protected MemberDto.Info info;
    protected String accessToken;
    protected String refreshToken;
    protected Authentication authentication;

    @BeforeEach
    void setUp() throws Exception {
        jwtTokenUtils = new JwtTokenUtils(SECRET_KEY, ACCESS_EXIRATION_MINUTE, REFRESH_EXIRATION_MINUTE);

        post = MemberDto.Post.builder()
                .email("hgd@gmail.com")
                .password("ghdrlfehd1!")
                .mbti("INFJ")
                .nickname("홍길동")
                .phoneNumber("010-1234-5678")
                .build();

        patch = MemberDto.Patch.builder()
                .mbti("INFJ")
                .nickname("홍길동")
                .build();

        info = MemberDto.Info.builder()
                .mbti("INFJ")
                .nickname("홍길동")
                .profileImage("https://seb40-main10-bucket.s3.ap-northeast-2.amazonaws.com/memberImage/default/TestImage.png")
                .build();

        member = Member.builder()
                .id(1L)
                .email("hgd@gmail.com")
                .roles(List.of("USER"))
                .nickname("홍길동")
                .phoneNumber("010-1234-5678")
                .build();

        accessToken = jwtTokenUtils.generateAccessToken(member);
        refreshToken = jwtTokenUtils.generateRefreshToken(member);


        List<GrantedAuthority> authorities = member.getRoles().stream()
                .map(role -> (GrantedAuthority) () -> "ROLE_" + role)
                .collect(Collectors.toList());

        authentication = new JwtAuthenticationToken(authorities, member.getEmail(), member.getId(), null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
