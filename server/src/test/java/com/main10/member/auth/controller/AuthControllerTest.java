package com.main10.member.auth.controller;


import com.google.gson.Gson;
import com.main10.domain.member.controller.AuthController;
import com.main10.domain.member.dto.AuthDto;
import com.main10.domain.member.service.AuthService;
import com.main10.domain.member.service.MemberDbService;
import com.main10.global.security.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WithMockUser
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@WebMvcTest({AuthController.class})
@MockBean(JpaMetamodelMappingContext.class)
public class AuthControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected Gson gson;

    @MockBean
    protected AuthService authService;

    @MockBean
    protected RedisUtils redisUtils;

    @MockBean
    protected MemberDbService memberDbService;

    AuthDto.Login login;
}
