package com.main10.review.controller;

import com.main10.domain.place.entity.Place;
import com.main10.domain.place.entity.PlaceImage;
import com.main10.domain.review.dto.ReviewDto;
import com.main10.domain.review.entity.Review;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.main10.utils.ApiDocumentUtils.getRequestPreProcessor;
import static com.main10.utils.ApiDocumentUtils.getResponsePreProcessor;
import static com.main10.utils.AuthConstants.AUTHORIZATION;
import static com.main10.utils.AuthConstants.REFRESH;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ReviewGetTest extends ReviewControllerTest {

    @Test
    @DisplayName("GET 장소 리뷰 조회")
    void getPlaceReview() throws Exception {

        Long placeId = 1L;

        int page = 1;
        int size = 5;

        Place place = Place.builder()
                .title("문의는 DM으로 받는 카페")
                .address("경기도 인스타시")
                .charge(1000000)
                .detailInfo("정중하게 질문해 주세요. 저희는 대답하는 기계가 아닙니다.")
                .endTime(23)
                .build();

        Review review = Review.builder()
                .id(1L)
                .score(0.5)
                .comment("그렇게 장사하지 마세요!!")
                .build();

        PlaceImage placeImage = PlaceImage.builder().filePath("image.jpg").build();
        place.addPlaceImage(placeImage);

        List<ReviewDto.Response> responses = new ArrayList<>();
        responses.add(new ReviewDto.Response(review, member));
        given(reviewService.getPlaceReviews(Mockito.anyLong(), Mockito.any(Pageable.class))).willReturn(new PageImpl<>(responses));

        ResultActions actions =
                mockMvc.perform(
                        get("/review/place/{place-id}", placeId)
                                .queryParam("page", String.valueOf(page))
                                .queryParam("size", String.valueOf(size))
                );
        actions
                .andExpect(status().isOk())
                .andDo(document("장소 상세페이지 리뷰 조회",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        pathParameters(
                                parameterWithName("place-id").description("장소 식별자")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.ARRAY).description("상세 정보 리뷰 데이터"),
                                        fieldWithPath("data[].reviewId").type(JsonFieldType.NUMBER).description("리뷰 식별자"),
                                        fieldWithPath("data[].nickname").type(JsonFieldType.STRING).description("닉네임"),
                                        fieldWithPath("data[].score").type(JsonFieldType.NUMBER).description("별점"),
                                        fieldWithPath("data[].comment").type(JsonFieldType.STRING).description("댓글"),
                                        fieldWithPath("data[].createdAt").type(JsonFieldType.STRING).description("생성일"),
                                        fieldWithPath("data[].profileImage").type(JsonFieldType.STRING).description("프로필 이미지"),

                                        fieldWithPath("pageInfo").type(JsonFieldType.OBJECT).description("페이지 정보"),
                                        fieldWithPath("pageInfo.page").type(JsonFieldType.NUMBER).description("페이지"),
                                        fieldWithPath("pageInfo.size").type(JsonFieldType.NUMBER).description("사이즈"),
                                        fieldWithPath("pageInfo.totalElements").type(JsonFieldType.NUMBER).description("총 갯수"),
                                        fieldWithPath("pageInfo.totalPages").type(JsonFieldType.NUMBER).description("총 페이지 수")
                                )
                        )
                ));
    }

    @Test
    @DisplayName("GET 마이페이지 리뷰 조회")
    void getMypageReview() throws Exception {

        int page = 1;
        int size = 5;

        Place place = Place.builder()
                .title("잠깐 화장실로 쓰실분 구해요.")
                .id(1L)
                .address("경기도 하노이시")
                .charge(200000)
                .detailInfo("화장실로 잠시 쓰실분은 연락 주세요.")
                .endTime(22)
                .build();

        Review review = Review.builder()
                .id(1L)
                .score(5.0)
                .comment("화장실이 너무 급해서 잠깐 이용했는데 만족했어요.")
                .build();

        PlaceImage placeImage = PlaceImage.builder().filePath("image.jpg").build();
        place.addPlaceImage(placeImage);

        List<ReviewDto.MyPage> myPages = new ArrayList<>();
        myPages.add(new ReviewDto.MyPage(review, place));
        given(reviewService.getReviewsMypage(Mockito.anyLong(), Mockito.any(Pageable.class))).willReturn(new PageImpl<>(myPages));

        ResultActions actions =
                mockMvc.perform(
                        get("/review")
                                .header(AUTHORIZATION, "Bearer " + accessToken)
                                .header(REFRESH, refreshToken)
                                .queryParam("page", String.valueOf(page))
                                .queryParam("size", String.valueOf(size))
                );

        actions
                .andExpect(status().isOk())
                .andDo(document(
                        "마이페이지 리뷰 조회",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("엑세스 토큰"),
                                headerWithName(REFRESH).description("리프레시 토큰")
                        ),
                        responseFields(
                                List.of(fieldWithPath("data").type(JsonFieldType.ARRAY).description("마이페이지 리뷰 데이터"),
                                        fieldWithPath("data[].reviewId").type(JsonFieldType.NUMBER).description("리뷰 식별자"),
                                        fieldWithPath("data[].title").type(JsonFieldType.STRING).description("제목"),
                                        fieldWithPath("data[].score").type(JsonFieldType.NUMBER).description("별점"),
                                        fieldWithPath("data[].comment").type(JsonFieldType.STRING).description("댓글"),
                                        fieldWithPath("data[].createdAt").type(JsonFieldType.STRING).description("생성일"),
                                        fieldWithPath("data[].placeId").type(JsonFieldType.NUMBER).description("장소 식별자"),
                                        fieldWithPath("data[].image").type(JsonFieldType.STRING).description("대표 이미지"),

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