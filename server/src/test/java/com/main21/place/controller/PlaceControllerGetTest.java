package com.main21.place.controller;

import com.main21.place.dto.PlaceCategoryDto;
import com.main21.place.dto.PlaceDto;
import com.main21.place.dto.PlaceResponseDto;
import com.main21.place.entity.PlaceImage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static com.main21.utils.ApiDocumentUtils.getRequestPreProcessor;
import static com.main21.utils.ApiDocumentUtils.getResponsePreProcessor;
import static com.main21.utils.AuthConstants.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PlaceControllerGetTest extends PlaceControllerTest{

    @Test
    @DisplayName("GET 장소 상세 조회")
    void getPlace() throws Exception{

        Long placeId = 1L;
        Boolean isBookmark = false;

        List<String> placeImages = new ArrayList<>();
        placeImages.add("image1.jpg");
        placeImages.add("image2.jpg");
        placeImages.add("image3.jpg");

        List<String> categories = new ArrayList<>();
        placeImages.add("캠핑장");
        placeImages.add("파티룸");
        placeImages.add("또 뭐있더라");

        PlaceResponseDto responseDto =
                new PlaceResponseDto(place, placeImages, categories, member, isBookmark);

        given(redisUtils.getId(Mockito.anyString())).willReturn(1L);
        given(placeService.searchPlace(Mockito.anyLong(), Mockito.anyString()))
                .willReturn(responseDto);

        ResultActions actions =
                mockMvc.perform(
                        get("/place/{place-id}", placeId)
                                .header(AUTHORIZATION, "Bearer " + accessToken)
                                .header(REFRESH, refreshToken)

                );

        actions
                .andExpect(status().isOk())
                .andDo(document(
                        "장소 상세 조회",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("엑세스 토큰"),
                                headerWithName(REFRESH).description("리프레시 토큰")
                        ),
                        pathParameters(
                                parameterWithName("place-id").description("장소 식별자")
                        ),
                        responseFields(
                                placeDefaultResponse()
                        )
                ));
    }

    @Test
    @DisplayName("GET 장소 전체 조회")
    void getPlacesPage() throws Exception{

        PlaceImage placeImage = PlaceImage.builder().filePath("image.jpg").build();
        place.addPlaceImage(placeImage);

        List<PlaceDto.Response> placeList = new ArrayList<>();
        placeList.add(new PlaceDto.Response(place));

        given(placeDbService.getPlacesPage(Mockito.any(Pageable.class)))
                .willReturn(new PageImpl<>(placeList));

        ResultActions actions =
                mockMvc.perform(
                        get("/home")
                );

        actions
                .andExpect(status().isOk())
                .andDo(document(
                        "장소 전체 조회",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        responseFields(
                                placePageResponse()
                        )
                ));
    }

    @Test
    @DisplayName("GET 장소 카테고리 조회")
    void getCategoryPage() throws Exception{

        Long categoryId = 1L;
        String title = "짱짱";

        PlaceImage placeImage = PlaceImage.builder().filePath("image.jpg").build();
        place.addPlaceImage(placeImage);

        List<PlaceCategoryDto.Response> placeList = new ArrayList<>();
        placeList.add(new PlaceCategoryDto.Response(place, categoryId));

        given(placeDbService.searchTitleCategory(Mockito.anyLong(), Mockito.anyString(), Mockito.any(Pageable.class)))
                .willReturn(new PageImpl<>(placeList));

        ResultActions actions =
                mockMvc.perform(
                        get("/category/{category-id}/search/{title:.+}", categoryId, title)
                );

        actions
                .andExpect(status().isOk())
                .andDo(document(
                        "장소 카테고리 조회",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        pathParameters(
                                parameterWithName("category-id").description("카테고리 식별자"),
                                parameterWithName("title").description("검색 키워드")
                        ),
                        responseFields(
                                placeCategoryPageResponse()
                        )
                ));

    }

    @Test
    @DisplayName("GET 장소 마이페이지 조회")
    void getPlaceMypage() throws Exception{

        PlaceImage placeImage = PlaceImage.builder().filePath("image.jpg").build();
        place.addPlaceImage(placeImage);

        List<PlaceDto.Response> placeList = new ArrayList<>();
        placeList.add(new PlaceDto.Response(place));

        given(redisUtils.getId(Mockito.anyString())).willReturn(1L);
        given(placeDbService.getPlaceMypage(Mockito.anyString(), Mockito.any(Pageable.class)))
                .willReturn(new PageImpl<>(placeList));

        ResultActions actions =
                mockMvc.perform(
                        get("/place")
                                .header(AUTHORIZATION, "Bearer " + accessToken)
                                .header(REFRESH, refreshToken)
                );

        actions
                .andExpect(status().isOk())
                .andDo(document(
                        "장소 마이페이지 조회",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("엑세스 토큰"),
                                headerWithName(REFRESH).description("리프레시 토큰")
                        ),
                        responseFields(
                                placePageResponse()
                        )
                ));
    }
}
