package com.main21.review.controller;

import com.main21.member.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
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
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ReviewDeleteTest extends ReviewControllerTest {

    @Test
    @DisplayName("DELETE 리뷰 삭제")
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
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("엑세스 토큰"),
                                headerWithName(REFRESH).description("리프레시 토큰")),
                        pathParameters(
                                parameterWithName("review-id").description("리뷰 식별자"))
                ));
    }
}