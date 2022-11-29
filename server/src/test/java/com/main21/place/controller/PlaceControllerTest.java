package com.main21.place.controller;

import com.google.gson.Gson;
import com.main21.member.entity.Member;
import com.main21.place.dto.PlaceCategoryDto;
import com.main21.place.dto.PlaceDto;
import com.main21.place.dto.PlaceResponseDto;
import com.main21.place.entity.Place;
import com.main21.place.entity.PlaceImage;
import com.main21.place.service.PlaceDbService;
import com.main21.place.service.PlaceService;
import com.main21.security.utils.JwtTokenUtils;
import com.main21.security.utils.RedisUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

import java.util.ArrayList;
import java.util.List;

import static com.main21.utils.ApiDocumentUtils.getRequestPreProcessor;
import static com.main21.utils.ApiDocumentUtils.getResponsePreProcessor;
import static com.main21.utils.AuthConstants.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@WebMvcTest({PlaceController.class})
@MockBean(JpaMetamodelMappingContext.class)
class PlaceControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    @MockBean
    private PlaceService placeService;

    @MockBean
    private PlaceDbService placeDbService;

    @MockBean
    private RedisUtils redisUtils;

    private JwtTokenUtils jwtTokenUtils;

    @BeforeEach
    void setUp() throws Exception {
        jwtTokenUtils = new JwtTokenUtils(SECRET_KEY, ACCESS_EXIRATION_MINUTE, REFRESH_EXIRATION_MINUTE);
    }

    @Test
    @WithMockUser
    @DisplayName("POST 장소 생성")
    void createPlace() throws Exception{

    }

    @Test
    @WithMockUser
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

        Place place = Place.builder()
                .title("짱짱 좋은 캠핑장")
                .address("경기도 뉴욕시")
                .charge(5000)
                .detailInfo("콘치즈 맛있어요")
                .endTime(23)
                .build();

        Member member = Member.builder()
                .email("hjd@gmail.com")
                .roles(List.of("USER"))
                .nickname("cornCheese")
                .phoneNumber("010-1234-5555")
                .build();

        String accessToken = jwtTokenUtils.generateAccessToken(member);
        String refreshToken = jwtTokenUtils.generateRefreshToken(member);

        PlaceResponseDto responseDto = new PlaceResponseDto(place, placeImages, categories, member, isBookmark);

        given(redisUtils.getId(Mockito.anyString())).willReturn(1L);
        given(placeService.searchPlace(Mockito.anyLong(), Mockito.anyString())).willReturn(responseDto);

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
                        responseFields(
                                List.of(
                                        fieldWithPath("placeId").type(JsonFieldType.NUMBER).description("장소 식별자").ignored(),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("장소 이름"),
                                        fieldWithPath("address").type(JsonFieldType.STRING).description("장소 주소"),
                                        fieldWithPath("detailInfo").type(JsonFieldType.STRING).description("장소 상세 정보"),
                                        fieldWithPath("endTime").type(JsonFieldType.NUMBER).description("장소 영업 종료 시간"),

                                        fieldWithPath("category").type(JsonFieldType.ARRAY).description("장소 카테고리 리스트"),

                                        fieldWithPath("maxCapacity").type(JsonFieldType.NUMBER).description("장소 최대 수용인원"),
                                        fieldWithPath("score").type(JsonFieldType.NUMBER).description("장소 평점"),
                                        fieldWithPath("charge").type(JsonFieldType.NUMBER).description("장소 시간당 가격"),

                                        fieldWithPath("filePath").type(JsonFieldType.ARRAY).description("장소 이미지 리스트"),

                                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("장소 작성자 닉네임"),
                                        fieldWithPath("phoneNumber").type(JsonFieldType.STRING).description("장소 작성자 전화번호"),

                                        fieldWithPath("bookmark").type(JsonFieldType.BOOLEAN).description("북마크 여부")
                                )
                        )
                ));

    }

    @Test
    @WithMockUser
    @DisplayName("PATCH 장소 수정")
    void patchPlace() throws Exception{

    }

    @Test
    @WithMockUser
    @DisplayName("GET 장소 전체 조회")
    void getPlacesPage() throws Exception{

        Place place = Place.builder()
                .title("짱짱 좋은 캠핑장")
                .address("경기도 뉴욕시")
                .charge(5000)
                .detailInfo("콘치즈 맛있어요")
                .endTime(23)
                .build();

        PlaceImage placeImage = PlaceImage.builder().filePath("image.jpg").build();
        place.addPlaceImage(placeImage);

        List<PlaceDto.Response> placeList = new ArrayList<>();
        placeList.add(new PlaceDto.Response(place));

        given(placeDbService.getPlacesPage(Mockito.any(Pageable.class))).willReturn(new PageImpl<>(placeList));

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
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.ARRAY).description("장소 데이터"),
                                        fieldWithPath("data[].placeId").type(JsonFieldType.NUMBER).description("장소 식별자").ignored(),
                                        fieldWithPath("data[].image").type(JsonFieldType.STRING).description("이미지"),
                                        fieldWithPath("data[].title").type(JsonFieldType.STRING).description("장소 이름"),
                                        fieldWithPath("data[].score").type(JsonFieldType.NUMBER).description("장소 평점"),
                                        fieldWithPath("data[].endTime").type(JsonFieldType.NUMBER).description("장소 영업 종료 시간"),
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

    @Test
    @WithMockUser
    @DisplayName("GET 장소 카테고리 조회")
    void getCategoryPage() throws Exception{

        Long categoryId = 1L;
        String title = "짱짱";

        int page = 1;
        int size = 5;

        Place place = Place.builder()
                .title("짱짱 좋은 캠핑장")
                .address("경기도 뉴욕시")
                .charge(5000)
                .detailInfo("콘치즈 맛있어요")
                .build();

        PlaceImage placeImage = PlaceImage.builder().filePath("image.jpg").build();
        place.addPlaceImage(placeImage);

        List<PlaceCategoryDto.Response> placeList = new ArrayList<>();
        placeList.add(new PlaceCategoryDto.Response(place, categoryId));

        given(placeDbService.searchTitleCategory(Mockito.anyLong(), Mockito.anyString(), Mockito.any(Pageable.class))).willReturn(new PageImpl<>(placeList));

        ResultActions actions =
                mockMvc.perform(
                        get("/category/{category-id}/search/{title:.+}", categoryId, title)
                                .queryParam("page", String.valueOf(page))
                                .queryParam("size", String.valueOf(size))
                );

        actions
                .andExpect(status().isOk())
                .andDo(document(
                        "장소 키테고리 조회",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.ARRAY).description("장소 데이터"),
                                        fieldWithPath("data[].categoryId").type(JsonFieldType.NUMBER).description("카테고리 식별자").ignored(),
                                        fieldWithPath("data[].placeId").type(JsonFieldType.NUMBER).description("장소 식별자").ignored(),
                                        fieldWithPath("data[].image").type(JsonFieldType.STRING).description("이미지"),
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

    @Test
    @WithMockUser
    @DisplayName("POST 장소 상세 검색")
    void searchDetail() throws Exception{

        int page = 1;
        int size = 5;

        PlaceDto.SearchDetail detail = PlaceDto.SearchDetail.builder()
                .startCharge(1000)
                .endCharge(50000)
                .capacity(4)
                .build();

        String content = gson.toJson(detail);

        Place place = Place.builder()
                .title("짱짱 좋은 캠핑장")
                .address("경기도 뉴욕시")
                .charge(5000)
                .detailInfo("콘치즈 맛있어요")
                .endTime(23)
                .build();

        PlaceImage placeImage = PlaceImage.builder().filePath("image.jpg").build();
        place.addPlaceImage(placeImage);

        List<PlaceDto.Response> placeList = new ArrayList<>();
        placeList.add(new PlaceDto.Response(place));

        given(placeDbService.searchDetail(Mockito.any(), Mockito.any(Pageable.class))).willReturn(new PageImpl<>(placeList));

        ResultActions actions =
                mockMvc.perform(
                        post("/search/detail")
                                .queryParam("page", String.valueOf(page))
                                .queryParam("size", String.valueOf(size))
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
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.ARRAY).description("장소 데이터"),
                                        fieldWithPath("data[].placeId").type(JsonFieldType.NUMBER).description("장소 식별자").ignored(),
                                        fieldWithPath("data[].image").type(JsonFieldType.STRING).description("이미지"),
                                        fieldWithPath("data[].title").type(JsonFieldType.STRING).description("장소 이름"),
                                        fieldWithPath("data[].score").type(JsonFieldType.NUMBER).description("장소 평점"),
                                        fieldWithPath("data[].endTime").type(JsonFieldType.NUMBER).description("장소 영업 종료 시간"),
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

    @Test
    @WithMockUser
    @DisplayName("GET 장소 잔체 검색 조회")
    void searchTitleAll() throws Exception{

        String title = "짱짱";

        int page = 1;
        int size = 5;

        Place place = Place.builder()
                .title("짱짱 좋은 캠핑장")
                .address("경기도 뉴욕시")
                .charge(5000)
                .detailInfo("콘치즈 맛있어요")
                .endTime(23)
                .build();

        PlaceImage placeImage = PlaceImage.builder().filePath("image.jpg").build();
        place.addPlaceImage(placeImage);

        List<PlaceDto.Response> placeList = new ArrayList<>();
        placeList.add(new PlaceDto.Response(place));

        given(placeDbService.searchTitleAll(Mockito.anyString(), Mockito.any(Pageable.class))).willReturn(new PageImpl<>(placeList));

        ResultActions actions =
                mockMvc.perform(
                        get("/search/{title:.+}", title)
                                .queryParam("page", String.valueOf(page))
                                .queryParam("size", String.valueOf(size))
                );

        actions
                .andExpect(status().isOk())
                .andDo(document(
                        "장소 전체 타이틀 검색",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.ARRAY).description("장소 데이터"),
                                        fieldWithPath("data[].placeId").type(JsonFieldType.NUMBER).description("장소 식별자").ignored(),
                                        fieldWithPath("data[].image").type(JsonFieldType.STRING).description("이미지"),
                                        fieldWithPath("data[].title").type(JsonFieldType.STRING).description("장소 이름"),
                                        fieldWithPath("data[].score").type(JsonFieldType.NUMBER).description("장소 평점"),
                                        fieldWithPath("data[].endTime").type(JsonFieldType.NUMBER).description("장소 영업 종료 시간"),
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

    @Test
    @WithMockUser
    @DisplayName("GET 장소 카테고리 검색 조회")
    void searchTitleCategory() throws Exception{
        Long categoryId = 1L;

        int page = 1;
        int size = 5;

        Place place = Place.builder()
                .title("짱짱 좋은 캠핑장")
                .address("경기도 뉴욕시")
                .charge(5000)
                .detailInfo("콘치즈 맛있어요")
                .build();

        PlaceImage placeImage = PlaceImage.builder().filePath("image.jpg").build();
        place.addPlaceImage(placeImage);

        List<PlaceCategoryDto.Response> placeList = new ArrayList<>();
        placeList.add(new PlaceCategoryDto.Response(place, categoryId));

        given(placeDbService.getCategoryPage(Mockito.anyLong(), Mockito.any(Pageable.class))).willReturn(new PageImpl<>(placeList));

        ResultActions actions =
                mockMvc.perform(
                        get("/category/{category-id}", categoryId)
                                .queryParam("page", String.valueOf(page))
                                .queryParam("size", String.valueOf(size))
                );

        actions
                .andExpect(status().isOk())
                .andDo(document(
                        "장소 키테고리 타이틀 검색",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.ARRAY).description("장소 데이터"),
                                        fieldWithPath("data[].categoryId").type(JsonFieldType.NUMBER).description("카테고리 식별자").ignored(),
                                        fieldWithPath("data[].placeId").type(JsonFieldType.NUMBER).description("장소 식별자").ignored(),
                                        fieldWithPath("data[].image").type(JsonFieldType.STRING).description("이미지"),
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

    @Test
    @WithMockUser
    @DisplayName("GET 장소 마이페이지 조회")
    void getPlaceMypage() throws Exception{

        int page = 1;
        int size = 5;

        Place place = Place.builder()
                .title("짱짱 좋은 캠핑장")
                .address("경기도 뉴욕시")
                .charge(5000)
                .detailInfo("콘치즈 맛있어요")
                .endTime(23)
                .build();

        Member member = Member.builder()
                .email("hjd@gmail.com")
                .roles(List.of("USER"))
                .nickname("cornCheese")
                .phoneNumber("010-1234-5555")
                .build();

        String accessToken = jwtTokenUtils.generateAccessToken(member);
        String refreshToken = jwtTokenUtils.generateRefreshToken(member);

        PlaceImage placeImage = PlaceImage.builder().filePath("image.jpg").build();
        place.addPlaceImage(placeImage);

        List<PlaceDto.Response> placeList = new ArrayList<>();
        placeList.add(new PlaceDto.Response(place));


        given(redisUtils.getId(Mockito.anyString())).willReturn(1L);
        given(placeDbService.getPlaceMypage(Mockito.anyString(), Mockito.any(Pageable.class))).willReturn(new PageImpl<>(placeList));

        ResultActions actions =
                mockMvc.perform(
                        get("/place")
                                .header(AUTHORIZATION, "Bearer " + accessToken)
                                .header(REFRESH, refreshToken)
                                .queryParam("page", String.valueOf(page))
                                .queryParam("size", String.valueOf(size))
                );

        actions
                .andExpect(status().isOk())
                .andDo(document(
                        "장소 마이페이지 조회",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.ARRAY).description("장 데이터"),
                                        fieldWithPath("data[].placeId").type(JsonFieldType.NUMBER).description("장소 식별자").ignored(),
                                        fieldWithPath("data[].image").type(JsonFieldType.STRING).description("이미지"),
                                        fieldWithPath("data[].title").type(JsonFieldType.STRING).description("장소 이름"),
                                        fieldWithPath("data[].score").type(JsonFieldType.NUMBER).description("장소 평점"),
                                        fieldWithPath("data[].endTime").type(JsonFieldType.NUMBER).description("장소 영업 종료 시간"),
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

    @Test
    @WithMockUser
    @DisplayName("DELETE 장소 삭제")
    void deleteHosting() throws Exception{

        Long placeId = 1L;

        Member member = Member.builder()
                .email("hjd@gmail.com")
                .roles(List.of("USER"))
                .build();

        String accessToken = jwtTokenUtils.generateAccessToken(member);
        String refreshToken = jwtTokenUtils.generateRefreshToken(member);

        given(redisUtils.getId(Mockito.anyString())).willReturn(1L);
        doNothing().when(placeService).deleteHosting(Mockito.anyString(), Mockito.anyLong());

        ResultActions actions =
                mockMvc.perform(
                        delete("/place/{place-id}", placeId)
                                .header(AUTHORIZATION, "Bearer " + accessToken)
                                .header(REFRESH, refreshToken)
                                .with(csrf())

                );

        actions
                .andExpect(status().isOk())
                .andDo(document("장소 삭제",
                        pathParameters(
                                parameterWithName("place-id").description("장소 식별자")
                        )
                ));

    }
}