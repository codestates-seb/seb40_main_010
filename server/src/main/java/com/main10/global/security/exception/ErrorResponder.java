package com.main10.global.security.exception;

import com.google.gson.Gson;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;

/**
 * 권한 인증 실패시 발생하는 예외 핸들러 메서드 클래스
 * @author mozzi327
 */
public class ErrorResponder {

    @SneakyThrows
    public static void sendErrorResponse(HttpServletResponse res, HttpStatus status) {
        Gson gson = new Gson();
        res.setContentType(MediaType.APPLICATION_JSON_VALUE);
        res.setStatus(status.value());
        res.getWriter().write(404);
        res.encodeRedirectURL("/login");
    }
}
