package com.main21.review.controller;

import com.google.gson.Gson;
import com.main21.batch.service.BatchService;
import com.main21.member.entity.Member;
import com.main21.place.entity.Place;
import com.main21.reserve.entity.Reserve;
import com.main21.review.dto.ReviewDto;
import com.main21.review.entity.Review;
import com.main21.review.repository.ReviewRepository;
import com.main21.review.service.ReviewService;
import com.main21.security.utils.JwtTokenUtils;
import com.main21.security.utils.RedisUtils;
import lombok.With;
import org.apache.tomcat.util.http.parser.Authorization;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.main21.utils.ApiDocumentUtils.getRequestPreProcessor;
import static com.main21.utils.ApiDocumentUtils.getResponsePreProcessor;
import static com.main21.utils.AuthConstants.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import redis.embedded.Redis;

import java.time.LocalDateTime;
import java.util.List;

import static com.main21.review.entity.QReview.review;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;


@AutoConfigureMockMvc
@AutoConfigureRestDocs
@WebMvcTest({ReviewController.class})
@MockBean(JpaMetamodelMappingContext.class)
@WithMockUser
class ReviewControllerTest {
    @Autowired
    private MockMvc mockMvc;



    @MockBean
    private ReviewService reviewService;

    @MockBean
    private ReviewRepository reviewRepository;

    private JwtTokenUtils jwtTokenUtils;

    @MockBean
    private RedisUtils redisUtils;
    @Autowired
    private Gson gson;


    @BeforeEach
    void setUp() throws Exception {
        gson = new Gson();
        jwtTokenUtils = new JwtTokenUtils(SECRET_KEY, ACCESS_EXIRATION_MINUTE, REFRESH_EXIRATION_MINUTE);

    }

    @Test
    @DisplayName("리뷰 POST")
    void postReview() throws Exception {
        Long placeId = 1L;
        Long reserveId = 1L;
        ReviewDto.Post post = ReviewDto.Post.builder()
                .score(4.5)
                .comment("진짜 좋아요!!")
                .build();
        Place place = Place.builder()
                .title("감성 있는 파티룸")
                .address("경기도 오사카시")
                .charge(100000)
                .build();
        Member member = Member.builder()
                .email("hgd@gmail.com")
                .roles(List.of("USER"))
                .build();

        String content = gson.toJson(post);
        String accessToken = jwtTokenUtils.generateAccessToken(member);
        String refreshToken = jwtTokenUtils.generateRefreshToken(member);

        given(redisUtils.getId(Mockito.anyString())).willReturn(1L);
        doNothing().when(reviewService).createReview(Mockito.any(ReviewDto.Post.class), Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong());
        ResultActions actions =
                mockMvc.perform(
                        post("/review/{place-id}/reserve/{reserve-id}",placeId, reserveId)
                                .header(AUTHORIZATION, "Bearer " + accessToken)
                                .header(REFRESH, refreshToken)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                                .with(csrf())
                );
        actions
                .andExpect(status().isCreated())
                .andDo(document("리뷰 추가",
                        pathParameters(
                                parameterWithName("place-id").description("장소 식별자"),
                                parameterWithName("reserve-id").description("예약 식별자")
                        ),
                        requestFields(
                                List.of(
                                        fieldWithPath("score").type(JsonFieldType.NUMBER).description("별점"),
                                        fieldWithPath("comment").type(JsonFieldType.STRING).description("댓글")
                        )
                )
                ));
    }
    @Test
    @DisplayName("리뷰 PATCH")
    void patchReview() throws Exception {
        Long reviewId = 1L;
        ReviewDto.Patch patch = ReviewDto.Patch.builder()
                .score(2.5)
                .comment("진짜 별로입니다..")
                .build();

        Place place = Place.builder()
                .title("감성 있는 파티룸")
                .address("경기도 오사카시")
                .charge(100000)
                .build();
        Member member = Member.builder()
                .email("hgd@gmail.com")
                .roles(List.of("USER"))
                .build();
        String content = gson.toJson(patch);
        String accessToken = jwtTokenUtils.generateAccessToken(member);
        String refreshToken = jwtTokenUtils.generateRefreshToken(member);
        given(redisUtils.getId(Mockito.anyString())).willReturn(1L);
        doNothing().when(reviewService).updateReview(Mockito.anyLong(), Mockito.any(ReviewDto.Patch.class),Mockito.anyString());
        ResultActions actions =
                mockMvc.perform(
                        patch("/review/{review-id}/edit", reviewId)
                                .header(AUTHORIZATION, "Bearer " + accessToken)
                                .header(REFRESH, refreshToken)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                                .with(csrf())
                );
        actions
                .andExpect(status().isOk())
                .andDo(document("리뷰 수정",
                        pathParameters(
                                parameterWithName("review-id").description("리뷰 식별자")
                        ),
                        requestFields(List.of(
                                fieldWithPath("score").type(JsonFieldType.NUMBER).description("별점"),
                                fieldWithPath("comment").type(JsonFieldType.STRING).description("댓글")
                            )
                        )
                        ));
    }
    @Test
    @DisplayName("리뷰 DELETE")
    void deleteReview() throws Exception {
        Long reviewId = 1L;
        Member member = Member.builder()
                .email("hgd@gmail.com")
                .roles(List.of("USER"))
                .build();

        String accessToken = jwtTokenUtils.generateAccessToken(member);
        String refreshToken = jwtTokenUtils.generateRefreshToken(member);
        given(redisUtils.getId(Mockito.anyString())).willReturn(1L);
        doNothing().when(reviewService).deleteReview(Mockito.anyLong(), Mockito.anyString());

        ResultActions actions =
                mockMvc.perform(
                        delete("/review/{review-id}", reviewId)
                                .header(AUTHORIZATION, "Bearer " + accessToken)
                                .header(REFRESH, refreshToken)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(csrf())
                );
        actions.andExpect(status().isNoContent())
                .andDo(document("리뷰 삭제",
                        pathParameters(
                                parameterWithName("review-id").description("리뷰 식별자"))
                ));
    }
}