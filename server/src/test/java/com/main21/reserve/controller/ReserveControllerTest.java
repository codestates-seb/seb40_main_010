package com.main21.reserve.controller;

import com.google.gson.Gson;
import com.main21.member.entity.Member;
import com.main21.reserve.entity.Reserve;
import com.main21.reserve.service.ReserveDbService;
import com.main21.reserve.service.ReserveService;
import com.main21.security.utils.JwtTokenUtils;
import com.main21.security.utils.RedisUtils;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static com.main21.utils.AuthConstants.*;

@WithMockUser
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@WebMvcTest({ReserveController.class})
@MockBean(JpaMetamodelMappingContext.class)
class ReserveControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected Gson gson;

    @MockBean
    protected ReserveService reserveService;

    @MockBean
    protected ReserveDbService reserveDbService;

    @MockBean
    protected RedisUtils redisUtils;

    protected JwtTokenUtils jwtTokenUtils;


    protected Reserve reserve;
    protected Member member;
    protected String accessToken;
    protected String refreshToken;

    @BeforeEach
    void setUp() throws Exception {
        jwtTokenUtils = new JwtTokenUtils(SECRET_KEY, ACCESS_EXIRATION_MINUTE, REFRESH_EXIRATION_MINUTE);

        member = Member.builder()
                .email("hjd@gmail.com")
                .roles(List.of("USER"))
                .nickname("cornCheese")
                .phoneNumber("010-1234-5555")
                .build();

        accessToken = jwtTokenUtils.generateAccessToken(member);
        refreshToken = jwtTokenUtils.generateRefreshToken(member);

        reserve = Reserve.builder()
                .placeId(1L)
                .memberId(1L)
                .capacity(3)
                .startTime(LocalDateTime.of(2022, 11, 30, 15, 00))
                .endTime(LocalDateTime.of(2022, 11, 30, 16, 00))
                .totalCharge(50000L)
                .build();
    }
}