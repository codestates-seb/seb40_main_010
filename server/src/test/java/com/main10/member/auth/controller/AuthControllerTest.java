package com.main10.member.auth.controller;

import com.google.gson.Gson;
import com.main10.domain.member.controller.AuthController;
import com.main10.domain.member.dto.AuthDto;
import com.main10.domain.member.dto.TokenDto;
import com.main10.domain.member.entity.Member;
import com.main10.domain.member.service.AuthService;
import com.main10.domain.member.service.MemberDbService;
import com.main10.global.security.dto.LoginDto;
import com.main10.global.security.provider.JwtAuthenticationProvider;
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
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

import static com.main10.utils.AuthConstants.*;

@WithMockUser
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@WebMvcTest({AuthController.class})
@MockBean(JpaMetamodelMappingContext.class)
public class AuthControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected Gson gson;

    @MockBean
    protected AuthService authService;

    @MockBean
    protected MemberDbService memberDbService;

    @MockBean
    protected RedisUtils redisUtils;

    @MockBean
    protected HttpServletResponse response;

    @MockBean
    protected JwtTokenUtils jwtTokenUtils;

    protected HttpHeaders headers;

    protected LoginDto login;

    protected AuthDto res;
    protected Member member;
    protected TokenDto.Response resToken;
    protected String accessToken;
    protected String refreshToken;
    protected Authentication authentication;

    @BeforeEach
    void setUp() {
        headers = new HttpHeaders();
        jwtTokenUtils = new JwtTokenUtils(SECRET_KEY, ACCESS_EXIRATION_MINUTE, REFRESH_EXIRATION_MINUTE);

        member = Member.builder()
                .id(1L)
                .email("hgd@gmail.com")
                .roles(List.of("USER"))
                .nickname("홍길동")
                .phoneNumber("010-1234-5678")
                .mbti("INFJ")
                .build();

        login = LoginDto.builder()
                .email("hgd@gmail.com")
                .password("ghdrlfehd")
                .build();

        res = AuthDto.builder()
                .nickname("홍길동")
                .email("hgd@gmail.com")
                .mbti("INFJ")
                .build();

        accessToken = jwtTokenUtils.generateAccessToken(member);
        refreshToken = jwtTokenUtils.generateRefreshToken(member);

        headers.add(AUTHORIZATION, accessToken);
        headers.add(REFRESH, refreshToken);

        resToken = TokenDto.Response.builder()
                .response(res)
                .headers(headers)
                .build();

        List<GrantedAuthority> authorities = member.getRoles() .stream()
                .map(role -> (GrantedAuthority) () -> "ROLE_" + role)
                .collect(Collectors.toList());

        authentication = new JwtAuthenticationToken(authorities, member.getEmail(), member.getId(), null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
