package com.main10.review.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.main10.domain.member.entity.Member;
import com.main10.domain.member.entity.MemberImage;
import com.main10.domain.place.entity.PlaceImage;
import com.main10.domain.review.controller.ReviewController;
import com.main10.domain.review.service.ReviewService;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.main10.utils.AuthConstants.*;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@WebMvcTest({ReviewController.class})
@MockBean(JpaMetamodelMappingContext.class)
@WithMockUser
class ReviewControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected ReviewService reviewService;

    protected JwtTokenUtils jwtTokenUtils;

    @MockBean
    protected RedisUtils redisUtils;

    @Autowired
    protected Gson gson;


    protected Member member;

    protected String accessToken;

    protected String refreshToken;

    protected Authentication authentication;

    @BeforeEach
    void setUp() throws Exception {

        gson = new Gson();
        jwtTokenUtils = new JwtTokenUtils(SECRET_KEY, ACCESS_EXIRATION_MINUTE, REFRESH_EXIRATION_MINUTE);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
        gson = gsonBuilder.setPrettyPrinting().create();

        member = Member.builder()
                .id(1L)
                .email("hgd@gmail.com")
                .roles(List.of("USER"))
                .nickname("cornCheese")
                .build();

        MemberImage memberImage = MemberImage.builder().filePath("image.jpg")
                .id(1L)
                .build();

        memberImage.setMember(member);
        member.addMemberImage(memberImage);

        accessToken = jwtTokenUtils.generateAccessToken(member);
        refreshToken = jwtTokenUtils.generateRefreshToken(member);

        List<GrantedAuthority> authorities = member.getRoles() .stream()
                .map(role -> (GrantedAuthority) () -> "ROLE_" + role)
                .collect(Collectors.toList());

        authentication = new JwtAuthenticationToken(authorities, member.getEmail(), member.getId(), null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}