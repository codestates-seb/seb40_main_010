package com.main10.global.security.handler;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 인증되지 않은 사용자가 url 요청했을 경우 발생하는 핸들러 클래스
 * @author mozzi327
 */
@Slf4j
@Component
public class MemberAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    @SneakyThrows
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "UnAuthorized");
    }
}
