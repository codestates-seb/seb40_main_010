package com.main10.bookmark.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.ResultActions;

import static com.main10.utils.AuthConstants.AUTHORIZATION;
import static com.main10.utils.AuthConstants.REFRESH;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BookmarkDeleteTest extends BookmarkControllerTest{

    @Test
    @DisplayName("POST 북마크 삭제 - 북마크가 존재하면 삭제하기")
    void deleteBookmark() throws Exception{

        Long placeId = 1L;

        given(redisUtils.getId(Mockito.anyString())).willReturn(1L);
        given(bookmarkService.createBookmark(Mockito.anyLong(), Mockito.anyString())).willReturn(false);

        ResultActions actions =
                mockMvc.perform(
                        RestDocumentationRequestBuilders.get("/bookmark/{place-id}", placeId)
                                .header(AUTHORIZATION, "Bearer " + accessToken)
                                .header(REFRESH, refreshToken)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                );

        actions
                .andExpect(status().isOk())
                .andDo(document("북마크 삭제",
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("엑세스 토큰"),
                                headerWithName(REFRESH).description("리프레시 토큰")
                        ),
                        pathParameters(
                                parameterWithName("place-id").description("장소 식별자")
                        )
                ));
    }
}
