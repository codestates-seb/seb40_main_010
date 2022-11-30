package com.main21.review.controller;

import com.main21.member.entity.Member;
import com.main21.place.entity.Place;
import com.main21.review.dto.ReviewDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static com.main21.utils.ApiDocumentUtils.getRequestPreProcessor;
import static com.main21.utils.ApiDocumentUtils.getResponsePreProcessor;
import static com.main21.utils.AuthConstants.AUTHORIZATION;
import static com.main21.utils.AuthConstants.REFRESH;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ReviewPatchTest extends ReviewControllerTest {

    @Test
    @DisplayName("PATCH 리뷰 수정")
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
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("엑세스 토큰"),
                                headerWithName(REFRESH).description("리프레시 토큰")
                        ),
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
}