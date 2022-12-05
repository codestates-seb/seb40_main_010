package com.main10.reserve.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.main10.domain.member.entity.Member;
import com.main10.domain.place.entity.Place;
import com.main10.domain.place.service.PlaceDbService;
import com.main10.domain.reserve.controller.ReserveController;
import com.main10.domain.reserve.entity.Reserve;
import com.main10.domain.reserve.service.ReserveDbService;
import com.main10.domain.reserve.service.ReserveService;
import com.main10.global.security.token.JwtAuthenticationToken;
import com.main10.global.security.utils.JwtTokenUtils;
import com.main10.global.security.utils.RedisUtils;
import com.main10.utils.LocalDateTimeSerializer;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.main10.utils.AuthConstants.*;

@WithMockUser
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@WebMvcTest({ReserveController.class})
@MockBean(JpaMetamodelMappingContext.class)
class ReserveControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    protected Gson gson;

    @MockBean
    protected ReserveService reserveService;

    @MockBean
    protected ReserveDbService reserveDbService;

    @MockBean
    protected PlaceDbService placeDbService;

    @MockBean
    protected RedisUtils redisUtils;

    protected JwtTokenUtils jwtTokenUtils;

    protected Reserve reserve;
    protected Member member;
    protected Place place;

    protected String accessToken;
    protected String refreshToken;

    protected Authentication authentication;

    @BeforeEach
    void setUp() throws Exception {
        jwtTokenUtils = new JwtTokenUtils(SECRET_KEY, ACCESS_EXIRATION_MINUTE, REFRESH_EXIRATION_MINUTE);

        member = Member.builder()
                .id(1L)
                .email("hjd@gmail.com")
                .roles(List.of("USER"))
                .nickname("cornCheese")
                .phoneNumber("010-1234-5555")
                .build();

        accessToken = jwtTokenUtils.generateAccessToken(member);
        refreshToken = jwtTokenUtils.generateRefreshToken(member);

        reserve = Reserve.builder()
                .id(1L)
                .placeId(1L)
                .memberId(1L)
                .capacity(3)
                .startTime(LocalDateTime.of(2022, 11, 30, 15, 00))
                .endTime(LocalDateTime.of(2022, 11, 30, 16, 00))
                .totalCharge(50000L)
                .build();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
        gson = gsonBuilder.setPrettyPrinting().create();

        List<GrantedAuthority> authorities = member.getRoles().stream()
                .map(role -> (GrantedAuthority) () -> "ROLE_" + role)
                .collect(Collectors.toList());

        authentication = new JwtAuthenticationToken(authorities, member.getEmail(), member.getId(), null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}