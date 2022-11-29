package com.main21.review.controller;

import com.google.gson.Gson;
import com.main21.batch.service.BatchService;
import com.main21.member.dto.MemberDto;
import com.main21.member.entity.Member;
import com.main21.place.entity.Place;
import com.main21.place.entity.PlaceImage;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.main21.place.entity.QPlaceImage.placeImage;
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
import java.util.ArrayList;
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
                        post("/review/{place-id}/reserve/{reserve-id}", placeId, reserveId)
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
        doNothing().when(reviewService).updateReview(Mockito.anyLong(), Mockito.any(ReviewDto.Patch.class), Mockito.anyString());
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

    @Test
    @DisplayName("상세페이지 리뷰 조회")
    void getPlaceReview() throws Exception {
        Long placeId = 1L;
        int page = 1;
        int size = 5;
//        ProfileImage profileImage = MemberDto.Info.builder()
        Member member = Member.builder()
                .nickname("홍길동")
                .email("hgd@gmail.com")
                .roles(List.of("USER"))
                .build();

        Place place = Place.builder()
                .title("문의는 DM으로 받는 카페")
                .address("경기도 인스타시")
                .charge(1000000)
                .detailInfo("정중하게 질문해 주세요. 저희는 대답하는 기계가 아닙니다.")
                .endTime(23)
                .build();

        Review review = Review.builder()
                .score(0.5)
                .comment("그렇게 장사하지 마세요!!")
                .build();

        PlaceImage placeImage = PlaceImage.builder().filePath("image.jpg").build();
        place.addPlaceImage(placeImage);

        List<ReviewDto.Response> responses = new ArrayList<>();
        responses.add(new ReviewDto.Response(review, member));
        given(redisUtils.getId(Mockito.anyString())).willReturn(1L);
        given(reviewService.getPlaceReviews(Mockito.anyLong(), Mockito.any(Pageable.class))).willReturn(new PageImpl<>(responses));

        ResultActions actions =
                mockMvc.perform(
                        get("/review/{place-id}", placeId)
                                .queryParam("page", String.valueOf(page))
                                .queryParam("size", String.valueOf(size))
                );

        actions
                .andExpect(status().isOk())
                .andDo(document(
                        "장소 상세페이지 리뷰 조회",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.ARRAY).description("리뷰 데이터"),
                                        fieldWithPath("data[].reviewId").type(JsonFieldType.NUMBER).description("리뷰 식별자").ignored(),
//                                      fieldWithPath("data[].profileImage").type(JsonFieldType.ARRAY).description("프로필 이미지"),
                                        fieldWithPath("data[].nickname").type(JsonFieldType.STRING).description("닉네임"),
                                        fieldWithPath("data[].score").type(JsonFieldType.NUMBER).description("별점"),
                                        fieldWithPath("data[].comment").type(JsonFieldType.STRING).description("댓글"),
                                        fieldWithPath("data[].createdAt").type(JsonFieldType.STRING).description("생성일").ignored(),

                                        fieldWithPath("pageInfo").type(JsonFieldType.OBJECT).description("페이지 정보"),
                                        fieldWithPath("pageInfo.page").type(JsonFieldType.NUMBER).description("페이지"),
                                        fieldWithPath("pageInfo.size").type(JsonFieldType.NUMBER).description("사이즈"),
                                        fieldWithPath("pageInfo.totalElements").type(JsonFieldType.NUMBER).description("총 갯수"),
                                        fieldWithPath("pageInfo.totalPages").type(JsonFieldType.NUMBER).description("총 페이지 수")
                                )
                        )
                ));
    }
}