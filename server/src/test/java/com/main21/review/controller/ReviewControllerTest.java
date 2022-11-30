package com.main21.review.controller;

import com.google.gson.Gson;
import com.main21.review.repository.ReviewRepository;
import com.main21.review.service.ReviewService;
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
import static com.main21.utils.AuthConstants.*;

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