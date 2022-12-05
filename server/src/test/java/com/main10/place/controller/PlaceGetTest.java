package com.main10.place.controller;

import com.main10.domain.place.dto.PlaceCategoryDto;
import com.main10.domain.place.dto.PlaceDto;
import com.main10.domain.place.entity.PlaceImage;
import com.main10.domain.reserve.dto.ReserveDto;
import com.main10.domain.reserve.entity.Reserve;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.main10.utils.ApiDocumentUtils.getRequestPreProcessor;
import static com.main10.utils.ApiDocumentUtils.getResponsePreProcessor;
import static com.main10.utils.AuthConstants.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PlaceGetTest extends PlaceControllerTest{

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

        Reserve reserve = Reserve.builder()
                .startTime(LocalDateTime.of(2022, 11, 30, 03, 00))
                .endTime(LocalDateTime.of(2022, 11, 30, 04, 00))
                .build();

        List<ReserveDto.Detail> reserves = List.of(ReserveDto.Detail.builder()
                        .startTime(reserve.getStartTime())
                        .endTime(reserve.getEndTime())
                .build());

        PlaceDto.DetailResponse responseDto =
                new PlaceDto.DetailResponse(place, placeImages, categories, member, isBookmark, reserves);
        given(placeService.searchPlace(Mockito.anyLong(), Mockito.anyLong()))
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

        PlaceImage placeImage = PlaceImage.builder().filePath("image.jpg").build();
        place.addPlaceImage(placeImage);

        List<PlaceCategoryDto.Response> placeList = new ArrayList<>();
        placeList.add(new PlaceCategoryDto.Response(place, categoryId));

        given(placeDbService.getCategoryPage(Mockito.anyLong(), Mockito.any(Pageable.class)))
                .willReturn(new PageImpl<>(placeList));

        ResultActions actions =
                mockMvc.perform(
                        get("/category/{category-id}", categoryId)
                );

        actions
                .andExpect(status().isOk())
                .andDo(document(
                        "장소 카테고리 조회",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        pathParameters(
                                parameterWithName("category-id").description("카테고리 식별자")
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
        given(placeDbService.getPlaceMypage(Mockito.anyLong(), Mockito.any(Pageable.class)))
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
