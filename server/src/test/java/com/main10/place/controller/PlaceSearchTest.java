package com.main10.place.controller;

import com.main10.domain.place.dto.PlaceCategoryDto;
import com.main10.domain.place.dto.PlaceDto;
import com.main10.domain.place.entity.PlaceImage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static com.main10.utils.ApiDocumentUtils.getRequestPreProcessor;
import static com.main10.utils.ApiDocumentUtils.getResponsePreProcessor;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PlaceSearchTest extends PlaceControllerTest{

    @Test
    @DisplayName("POST 장소 상세 검색")
    void searchDetail() throws Exception{

        PlaceDto.SearchDetail detail = PlaceDto.SearchDetail.builder()
                .startCharge(1000)
                .endCharge(50000)
                .capacity(4)
                .build();

        String content = gson.toJson(detail);

        PlaceImage placeImage = PlaceImage.builder().filePath("image.jpg").build();
        place.addPlaceImage(placeImage);

        List<PlaceDto.Response> placeList = new ArrayList<>();
        placeList.add(new PlaceDto.Response(place));

        given(placeDbService.searchDetail(Mockito.any(), Mockito.any(Pageable.class)))
                .willReturn(new PageImpl<>(placeList));

        ResultActions actions =
                mockMvc.perform(
                        post("/search/detail")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                                .with(csrf())
                );

        actions
                .andExpect(status().isOk())
                .andDo(document(
                        "장소 상세 검색",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestFields(
                                List.of(
                                        fieldWithPath("startCharge").type(JsonFieldType.NUMBER).description("최소 가격").optional(),
                                        fieldWithPath("endCharge").type(JsonFieldType.NUMBER).description("최대 가격").optional(),
                                        fieldWithPath("capacity").type(JsonFieldType.NUMBER).description("인원수").optional()
                                )
                        ),
                        responseFields(
                                placePageResponse()
                        )
                ));
    }

    @Test
    @DisplayName("GET 장소 전체 검색 조회")
    void searchTitleAll() throws Exception{

        String title = "짱짱";

        PlaceImage placeImage = PlaceImage.builder().filePath("image.jpg").build();
        place.addPlaceImage(placeImage);

        List<PlaceDto.Response> placeList = new ArrayList<>();
        placeList.add(new PlaceDto.Response(place));

        given(placeDbService.searchTitleAll(Mockito.anyString(), Mockito.any(Pageable.class)))
                .willReturn(new PageImpl<>(placeList));

        ResultActions actions =
                mockMvc.perform(
                        get("/search/{title:.+}", title)
                );

        actions
                .andExpect(status().isOk())
                .andDo(document(
                        "장소 전체 타이틀 검색",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        pathParameters(
                                parameterWithName("title").description("검색 키워드")
                        ),
                        responseFields(
                                placePageResponse()
                        )
                ));
    }

    @Test
    @DisplayName("GET 장소 카테고리 검색 조회")
    void searchTitleCategory() throws Exception{

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
                        "장소 카테고리 타이틀 검색",
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

}
