package com.main10.global.security.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * OAuth2 로그인 실패 핸들러 클래스
 * @author mozzi327
 */
@Slf4j
@Component
public class OAuth2FailureHandler implements AuthenticationFailureHandler {
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    /**
     * 로그인 실패 핸들러 메서드
     *
     * @param req 요청 정보
     * @param res 응답 정보
     * @param exception 예외 정보
     * @author mozzi327
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest req,
                                        HttpServletResponse res,
                                        AuthenticationException exception) throws IOException {
        log.info("로그인 실패 시작");
        log.info("exception ---------> : {}" ,exception.getMessage());
        res.sendError(HttpServletResponse.SC_FORBIDDEN);
        log.info("로그인 실패 종료");
    }
}
