package com.main10.member.member.controller;

import com.main10.global.file.S3Upload;
import com.main10.global.file.UploadFile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.multipart.MultipartFile;
import static com.main10.utils.ApiDocumentUtils.getRequestPreProcessor;
import static com.main10.utils.ApiDocumentUtils.getResponsePreProcessor;
import static com.main10.utils.AuthConstants.AUTHORIZATION;
import static com.main10.utils.AuthConstants.REFRESH;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.multipart;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UploadProfileImageTest extends MemberControllerTest {

    @MockBean
    S3Upload s3Upload;

    @Test
    @DisplayName("멤버 프로필 수정 테스트")
    public void createMemberImageS3Test() throws Exception {

        MockMultipartFile files =
                new MockMultipartFile("file", "test.jpg", "image/jpg", "<<jpg data>>".getBytes());

        UploadFile profile = UploadFile.builder()
                .originFileName("test.jpg")
                .fileName("fcfd0796-d72f-4226-bf43-cd3048dacf39.jpeg")
                .filePath("https://아마존주소/memberImage/fcfd0796-d72f-4226-bf43-cd3048dacf39.jpeg")
                .fileSize(58698L)
                .build();

        given(s3Upload.uploadfile(Mockito.any(MultipartFile.class), Mockito.anyString())).willReturn(profile);
        doNothing().when(memberService).createProfileS3(Mockito.anyLong(), Mockito.any(MultipartFile.class));

        ResultActions actions = mockMvc.perform(multipart("/member/profile")
                .file(files)
                .header(AUTHORIZATION, "Bearer " + accessToken)
                .header(REFRESH, refreshToken)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .characterEncoding("UTF-8")
                .with(csrf())
        );

        actions.andExpect(status().isOk())
                .andDo(document(
                        "회원 프로필 이미지 수정",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("액세스 토큰"),
                                headerWithName(REFRESH).description("리프레시 토큰")
                        ),
                        requestParts(
                                partWithName("file").description("요청 프로필 이미지")
                        )));
    }
}
