package com.main21.member.controller;

import com.main21.member.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;


    /**
     * 사용자 로그아웃을 위한 컨트롤러 호출 메서드
     * @param refreshToken 리프레시 토큰
     * @param res HttpServletResponse
     * @author mozzi327
     */
    @DeleteMapping("/logout")
    public void logoutMember(@CookieValue(name = "memberId") Long memberId,
                             @CookieValue(name = "refreshToken") String refreshToken,
                             HttpServletResponse res) {
        authService.logoutMember(memberId, refreshToken, res);
    }



}
