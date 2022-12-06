package com.main10.domain.member.controller;

import com.main10.domain.member.dto.AuthDto;
import com.main10.domain.member.dto.TokenDto;
import com.main10.domain.member.service.AuthService;
import com.main10.global.security.dto.LoginDto;
import com.main10.global.security.token.JwtAuthenticationToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import static com.main10.domain.member.utils.AuthConstant.*;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    /**
     * 사용자 로그인을 위한 컨트롤러 호출 메서드
     * @param loginDto 로그인 정보
     * @return ResponseEntity
     * @author mozzi327
     */
    @PostMapping("/login")
    public ResponseEntity<AuthDto> loginMember(@RequestBody @Valid LoginDto loginDto) {
        TokenDto.Response response = authService.loginMember(loginDto);
        return ResponseEntity.ok()
                .headers(response.getHeaders())
                .body(response.getResponse());
    }

    /**
     * 사용자 로그아웃을 위한 컨트롤러 호출 메서드(레디스용)
     *
     * @param accessToken  액세스 토큰
     * @param refreshToken 리프레시 토큰
     * @param authentication 사용자 인증 정보
     * @author mozzi327
     */
    @DeleteMapping("/logout")
    public ResponseEntity<?> logoutMember(@RequestHeader(AUTHORIZATION) String accessToken,
                                       @RequestHeader(REFRESH_TOKEN) String refreshToken,
                                       Authentication authentication) {
        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
        authService.logoutMember(accessToken, refreshToken, token.getId());
        return ResponseEntity.ok().build();
    }

    /**
     * 사용자 액세스 토큰 리이슈를 위한 컨트롤러 호출 메서드
     * @param accessToken 액세스 토큰
     * @param refreshToken 리프레시 토큰
     * @param authentication 사용자 인증 정보
     * @return ResponseEntity
     * @author mozzi327
     */
    @GetMapping("/re-issue")
    public ResponseEntity<AuthDto> reIssueToken(@RequestHeader(AUTHORIZATION) String accessToken,
                                       @RequestHeader(REFRESH_TOKEN) String refreshToken,
                                       Authentication authentication) {
        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;

        TokenDto.Response response = authService.reIssueToken(accessToken, refreshToken, token.getId());
        return ResponseEntity.ok()
                .headers(response.getHeaders())
                .body(response.getResponse());
    }
}
