package com.main10.place.controller;

import com.main10.domain.place.dto.PlaceDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static com.main10.utils.AuthConstants.AUTHORIZATION;
import static com.main10.utils.AuthConstants.REFRESH;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.multipart;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PlacePostTest extends PlaceControllerTest{

    @Test
    @DisplayName("POST 장소 생성")
    void createPlace() throws Exception {

        List<String> categoryList = new ArrayList<>();
        categoryList.add("공유오피스");
        categoryList.add("바다근처");

        PlaceDto.Create key = PlaceDto.Create.builder()
                .score(0.0)
                .title("공유 스튜디오")
                .categoryList(categoryList)
                .maxCapacity(4)
                .address("서울 마포구")
                .detailInfo("조용한 스튜디오")
                .charge(5000)
                .endTime(22)
                .build();

        MockMultipartFile files =
                new MockMultipartFile("file", "test.jpg", "image/jpg", "<<jpg data>>".getBytes());

        String content = gson.toJson(key);
        doNothing().when(placeService).createPlace(Mockito.any(PlaceDto.Create.class), Mockito.anyLong(), Mockito.anyList());

        ResultActions actions =
                mockMvc.perform(
                        multipart("/place/post")
                                .file(files)
                                .file(new MockMultipartFile("key", "", "application/json", content.getBytes(StandardCharsets.UTF_8)))
                                .contentType("multipart/form-data")
                                .accept(MediaType.APPLICATION_JSON)
                                .content(content)
                                .header(AUTHORIZATION, "Bearer " + accessToken)
                                .header(REFRESH, refreshToken)
                                .with(csrf())
                );
        actions
                .andExpect(status().isOk())
                //.andReturn();
                .andDo(document(
                        "장소 등록",
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("엑세스 토큰"),
                                headerWithName(REFRESH).description("리프레시 토큰")
                        ),
                        requestFields(
                                List.of(
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("장소 이름"),
                                        fieldWithPath("score").type(JsonFieldType.NUMBER).description("장소 평점"),
                                        fieldWithPath("endTime").type(JsonFieldType.NUMBER).description("장소 영업 종료 시간"),
                                        fieldWithPath("charge").type(JsonFieldType.NUMBER).description("장소 시간당 가격"),
                                        fieldWithPath("address").type(JsonFieldType.STRING).description("장소 주소"),
                                        fieldWithPath("detailInfo").type(JsonFieldType.STRING).description("상세 정보"),
                                        fieldWithPath("maxCapacity").type(JsonFieldType.NUMBER).description("수용 인원"),
                                        fieldWithPath("categoryList").type(JsonFieldType.ARRAY).description("카테고리 정보")
                                )
                        )
                ));
    }
}
