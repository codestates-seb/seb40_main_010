package com.main10.review.controller;

import com.google.gson.Gson;
import com.main10.domain.review.controller.ReviewController;
import com.main10.domain.review.repository.ReviewRepository;
import com.main10.domain.review.service.ReviewService;
import com.main10.global.security.utils.JwtTokenUtils;
import com.main10.global.security.utils.RedisUtils;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
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

    @MockBean
    protected ReviewRepository reviewRepository;

    protected JwtTokenUtils jwtTokenUtils;

    @MockBean
    protected RedisUtils redisUtils;

    @Autowired
    protected Gson gson;

    @BeforeEach
    void setUp() throws Exception {
        gson = new Gson();
        jwtTokenUtils = new JwtTokenUtils(SECRET_KEY, ACCESS_EXIRATION_MINUTE, REFRESH_EXIRATION_MINUTE);
    }
}