package com.main10.bookmark.controller;

import com.main10.domain.bookmark.dto.BookmarkDto;
import com.main10.domain.bookmark.entity.Bookmark;
import com.main10.domain.place.entity.Place;
import com.main10.domain.place.entity.PlaceImage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BookmarkGetTest extends BookmarkControllerTest{

    @Test
    @DisplayName("GET 북마크 조회")
    void getBookmark() throws Exception{
        Bookmark bookmark = Bookmark.builder().id(1L).placeId(1L).memberId(1L).build();

        Place place = Place.builder()
                .title("짱짱 좋은 캠핑장")
                .address("경기도 뉴욕시")
                .charge(5000)
                .build();

        place.setId(1L);

        PlaceImage placeImage = PlaceImage.builder().filePath("image.jpg").build();
        place.addPlaceImage(placeImage);

        List<BookmarkDto.Response> bookmarkList = new ArrayList<>();
        bookmarkList.add(new BookmarkDto.Response(bookmark, place));

        given(bookmarkService.getBookmark(Mockito.anyLong(), Mockito.any(Pageable.class)))
                .willReturn(new PageImpl<>(bookmarkList));

        ResultActions actions =
                mockMvc.perform(
                        get("/bookmark")
                                .header(AUTHORIZATION, "Bearer " + accessToken)
                                .header(REFRESH, refreshToken)
                                .with(csrf())
                );

        actions
                .andExpect(status().isOk())
                .andDo(document(
                        "북마크 전체 조회",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("엑세스 토큰"),
                                headerWithName(REFRESH).description("리프레시 토큰")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.ARRAY).description("북마크 데이터"),
                                        fieldWithPath("data[].bookmarkId").type(JsonFieldType.NUMBER).description("북마크 식별자"),
                                        fieldWithPath("data[].placeId").type(JsonFieldType.NUMBER).description("장소 식별자"),
                                        fieldWithPath("data[].image").type(JsonFieldType.STRING).description("이미지"),
                                        fieldWithPath("data[].bookmarkUrl").type(JsonFieldType.STRING).description("북마크 URL"),
                                        fieldWithPath("data[].title").type(JsonFieldType.STRING).description("장소 이름"),
                                        fieldWithPath("data[].score").type(JsonFieldType.NUMBER).description("장소 평점"),
                                        fieldWithPath("data[].charge").type(JsonFieldType.NUMBER).description("장소 시간당 가격"),
                                        fieldWithPath("data[].address").type(JsonFieldType.STRING).description("장소 주소"),

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
