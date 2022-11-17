package com.main21.member.controller;

import com.main21.member.dto.AuthDto;
import com.main21.member.service.AuthService;
import com.main21.security.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.main21.security.utils.AuthConstants.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final JwtTokenUtils jwtTokenUtils;

    /**
     * 사용자 로그아웃을 위한 컨트롤러 호출 메서드(레디스용)
     *
     * @param refreshToken 리프레시 토큰
     * @param res          HttpServletResponse
     * @author mozzi327
     */
    @DeleteMapping("/logout")
    public void logoutMember(@CookieValue(name = MEMBER_ID) Long memberId,
                             @CookieValue(name = REFRESH_TOKEN) String refreshToken,
                             HttpServletResponse res) {
        authService.logoutMember(memberId, refreshToken, res);
    }


    /**
     * 사용자 로그아웃을 위한 컨트롤러 호출 메서드(임시)
     *
     * @param memberId     사용자 식별자
     * @param refreshToken 리프레시 토큰
     * @param res          HttpServletResponse
     * @author mozzi327
     */
    @DeleteMapping("/logout/bydb")
    public void logoutMemberByDb(@CookieValue(name = MEMBER_ID) Long memberId,
                                 @CookieValue(name = REFRESH_TOKEN) String refreshToken,
                                 HttpServletResponse res) {
        authService.logoutMemberByDb(memberId, refreshToken, res);
    }


    @GetMapping("/re-issue")
    public ResponseEntity reIssueToken(@CookieValue(name = MEMBER_ID) Long memberId,
                                       @CookieValue(name = REFRESH_TOKEN) String refreshToken) {

        AuthDto.Response response = authService.reIssueToken(memberId, refreshToken);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/test")
    public ResponseEntity authenticateForMember(HttpServletRequest request) {
        String accessToken = request.getHeader(AUTHORIZATION);
        String email = jwtTokenUtils.getEmail(accessToken);
        return ResponseEntity.ok(email);
    }
}
