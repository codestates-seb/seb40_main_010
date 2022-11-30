package com.main21.member.controller;

import com.main21.member.dto.AuthDto;
import com.main21.member.service.AuthService;
import com.main21.security.dto.LoginDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;

import static com.main21.security.utils.AuthConstants.*;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    /**
     * 사용자 로그인을 위한 컨트롤러 호출 메서드
     * @param loginDto 로그인 정보
     * @param res 응답
     * @return ResponseEntity
     * @author mozzi327
     */
    @PostMapping("/login")
    public ResponseEntity loginMember(@RequestBody LoginDto loginDto,
                                      HttpServletResponse res) {
        AuthDto.Response response = authService.loginMember(loginDto, res);
        return ResponseEntity.ok().body(response);
    }

    /**
     * 사용자 로그아웃을 위한 컨트롤러 호출 메서드(레디스용)
     *
     * @param accessToken  액세스 토큰
     * @param refreshToken 리프레시 토큰
     * @author mozzi327
     */
    @DeleteMapping("/logout")
    public ResponseEntity logoutMember(@RequestHeader(AUTHORIZATION) String accessToken,
                                       @RequestHeader(REFRESH_TOKEN) String refreshToken) {
        authService.logoutMember(accessToken, refreshToken);
        return ResponseEntity.ok().build();
    }

    /**
     * 사용자 액세스 토큰 리이슈를 위한 컨트롤러 호출 메서드
     * @param accessToken 액세스 토큰
     * @param refreshToken 리프레시 토큰
     * @return ResponseEntity
     * @author mozzi327
     */
    @GetMapping("/re-issue")
    public ResponseEntity reIssueToken(@RequestHeader(AUTHORIZATION) String accessToken,
                                       @RequestHeader(REFRESH_TOKEN) String refreshToken,
                                       HttpServletResponse res) {
        AuthDto.Response response = authService.reIssueToken(accessToken, refreshToken, res);
        return ResponseEntity.ok(response);
    }
}
