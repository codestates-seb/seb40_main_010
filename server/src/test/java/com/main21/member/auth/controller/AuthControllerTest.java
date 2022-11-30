package com.main21.member.auth.controller;


import com.google.gson.Gson;
import com.main21.member.controller.AuthController;
import com.main21.member.dto.AuthDto;
import com.main21.member.service.AuthService;
import com.main21.member.service.MemberDbService;
import com.main21.security.utils.RedisUtils;
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
