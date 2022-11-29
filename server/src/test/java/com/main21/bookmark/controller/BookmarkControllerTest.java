package com.main21.bookmark.controller;

import com.google.gson.Gson;
import com.main21.bookmark.dto.BookmarkDto;
import com.main21.bookmark.entity.Bookmark;
import com.main21.bookmark.service.BookmarkService;
import com.main21.member.entity.Member;
import com.main21.place.entity.Place;
import com.main21.place.entity.PlaceImage;
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
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static com.main21.utils.ApiDocumentUtils.getRequestPreProcessor;
import static com.main21.utils.ApiDocumentUtils.getResponsePreProcessor;
import static com.main21.utils.AuthConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@WebMvcTest({BookmarkController.class})
@MockBean(JpaMetamodelMappingContext.class)
class BookmarkControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    @MockBean
    private BookmarkService bookmarkService;

    @MockBean
    private PlaceService placeService;

    @MockBean
    private RedisUtils redisUtils;

    private JwtTokenUtils jwtTokenUtils;

    @BeforeEach
    void setUp() throws Exception {
        jwtTokenUtils = new JwtTokenUtils(SECRET_KEY, ACCESS_EXIRATION_MINUTE, REFRESH_EXIRATION_MINUTE);
    }

    @Test
    @WithMockUser
    @DisplayName("GET 북마크 조회")
    void getBookmark() throws Exception{

        int page = 1;
        int size = 5;

        Bookmark bookmark1 = Bookmark.builder().placeId(1L).memberId(1L).build();
        Bookmark bookmark2 = Bookmark.builder().placeId(2L).memberId(2L).build();

        PlaceImage placeImage = PlaceImage.builder().filePath("image.jpg").build();

        Place place1 = Place.builder()
                .title("짱짱 좋은 캠핑장")
                .address("경기도 뉴욕시")
                .charge(5000)
                .build();

        Place place2 = Place.builder()
                .title("짱짱 멋있는 파티룸")
                .address("강원도 런던시 221B")
                .charge(5000)
                .build();

        place1.addPlaceImage(placeImage);
        place2.addPlaceImage(placeImage);

        List<BookmarkDto.Response> bookmarkList = new ArrayList<>();
        bookmarkList.add(new BookmarkDto.Response(bookmark1, place1));
        bookmarkList.add(new BookmarkDto.Response(bookmark2, place2));

        Member member = Member.builder()
                .email("hjd@gmail.com")
                .roles(List.of("USER"))
                .build();

        String accessToken = jwtTokenUtils.generateAccessToken(member);
        String refreshToken = jwtTokenUtils.generateRefreshToken(member);

        given(redisUtils.getId(Mockito.anyString())).willReturn(1L);
        given(bookmarkService.getBookmark(Mockito.anyString(), Mockito.any(Pageable.class)))
                .willReturn(new PageImpl<>(bookmarkList));

        ResultActions actions =
                mockMvc.perform(
                        get("/bookmark")
                                .header(AUTHORIZATION, "Bearer " + accessToken)
                                .header(REFRESH, refreshToken)
                                .queryParam("page", String.valueOf(page))
                                .queryParam("size", String.valueOf(size))

                );

        actions
                .andExpect(status().isOk())
                .andDo(document(
                "북마크 전체 조회",
                getRequestPreProcessor(),
                getResponsePreProcessor(),
                responseFields(
                        List.of(
                                fieldWithPath("data").type(JsonFieldType.ARRAY).description("북마크 데이터"),
                                fieldWithPath("data[].bookmarkId").type(JsonFieldType.NUMBER).description("북마크 식별자").ignored(),
                                fieldWithPath("data[].placeId").type(JsonFieldType.NUMBER).description("장소 식별자").ignored(),
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

    @Test
    @WithMockUser
    @DisplayName("POST 북마크 생성 - 북마크가 존재하지 않으면 생성하기")
    void createBookmark() throws Exception{

        Long placeId = 1L;

        Member member = Member.builder()
                .email("hjd@gmail.com")
                .roles(List.of("USER"))
                .build();

        String accessToken = jwtTokenUtils.generateAccessToken(member);
        String refreshToken = jwtTokenUtils.generateRefreshToken(member);

        given(redisUtils.getId(Mockito.anyString())).willReturn(1L);
        if (bookmarkService.getBookmark(Mockito.anyString(), Mockito.any()) == null) {
            given(bookmarkService.createBookmark(Mockito.anyLong(), Mockito.anyString())).willReturn(true);
        }

        assertTrue(bookmarkService.createBookmark(Mockito.anyLong(), Mockito.anyString()));


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
                .andDo(document("북마크 추가",
                        pathParameters(
                                parameterWithName("place-id").description("장소 식별자")
                        )
                ));
    }


    @Test
    @WithMockUser
    @DisplayName("POST 북마크 삭제 - 북마크가 존재하면 삭제하기")
    void deleteBookmark() throws Exception{

        Long placeId = 1L;

        Member member = Member.builder()
                .email("hjd@gmail.com")
                .roles(List.of("USER"))
                .build();

        String accessToken = jwtTokenUtils.generateAccessToken(member);
        String refreshToken = jwtTokenUtils.generateRefreshToken(member);

        given(redisUtils.getId(Mockito.anyString())).willReturn(1L);
        if (bookmarkService.getBookmark(Mockito.anyString(), Mockito.any()) != null) {
            given(bookmarkService.createBookmark(Mockito.anyLong(), Mockito.anyString())).willReturn(false);
        }

        assertFalse(bookmarkService.createBookmark(Mockito.anyLong(), Mockito.anyString()));


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
                .andDo(document("북마크 추가",
                        pathParameters(
                                parameterWithName("place-id").description("장소 식별자")
                        )
                ));
    }
}