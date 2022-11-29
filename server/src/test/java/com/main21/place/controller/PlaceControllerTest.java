package com.main21.place.controller;

import com.google.gson.Gson;
import com.main21.member.entity.Member;
import com.main21.place.entity.Place;
import com.main21.place.repository.PlaceRepository;
import com.main21.place.service.PlaceDbService;
import com.main21.place.service.PlaceService;
import com.main21.security.utils.JwtTokenUtils;
import com.main21.security.utils.RedisUtils;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.main21.utils.AuthConstants.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

@WithMockUser
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@WebMvcTest({PlaceController.class})
@MockBean(JpaMetamodelMappingContext.class)
class PlaceControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected Gson gson;

    @MockBean
    protected PlaceService placeService;

    @MockBean
    protected PlaceDbService placeDbService;

    @MockBean
    protected RedisUtils redisUtils;

    protected JwtTokenUtils jwtTokenUtils;


    protected Place place;
    protected Member member;

    protected String accessToken;

    protected String refreshToken;

    @BeforeEach
    void setUp() throws Exception {
        jwtTokenUtils = new JwtTokenUtils(SECRET_KEY, ACCESS_EXIRATION_MINUTE, REFRESH_EXIRATION_MINUTE);

        member = Member.builder()
                .email("hjd@gmail.com")
                .roles(List.of("USER"))
                .nickname("cornCheese")
                .phoneNumber("010-1234-5555")
                .build();

        accessToken = jwtTokenUtils.generateAccessToken(member);
        refreshToken = jwtTokenUtils.generateRefreshToken(member);

        place = Place.builder()
                .title("짱짱 좋은 캠핑장")
                .address("경기도 뉴욕시")
                .charge(5000)
                .detailInfo("콘치즈 맛있어요")
                .endTime(23)
                .build();


    }

    protected List<FieldDescriptor> placePageResponse() {
        return List.of(
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
        );
    }

    protected List<FieldDescriptor> placeCategoryPageResponse() {
        return List.of(
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
        );
    }
    protected List<FieldDescriptor> placeDefaultResponse() {
        return List.of(
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
        );
    }
}