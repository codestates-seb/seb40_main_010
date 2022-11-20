package com.main21.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.main21.member.dto.AuthDto;
import com.main21.member.entity.Member;
import com.main21.security.dto.LoginDto;
import com.main21.security.utils.CookieUtils;
import com.main21.security.utils.JwtTokenUtils;
import com.main21.security.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import static com.main21.security.utils.AuthConstants.*;

/**
 * 사용자 인증을 위한 AuthenticationFilter 클래스
 *
 * @author mozzi327
 */
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtils jwtTokenUtils;
    private final CookieUtils cookieUtils;
    private final RedisUtils redisUtils;

    /* ----------------- Redis 토큰 전용 로직 --------------------

       ----------------- Redis 토큰 전용 로직 -------------------- */


    /**
     * request에 요청 스트림을 추출해서 권한 인증을 authenticationManager에게 전달하는 메서드
     *
     * @param req 요청
     * @param res 응답
     * @return Authentication
     * @author mozzi327
     */
    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) {
        ObjectMapper objectMapper = new ObjectMapper();
        LoginDto loginDto = objectMapper.readValue(req.getInputStream(), LoginDto.class);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());

        return authenticationManager.authenticate(authenticationToken);
    }


    /**
     * 인증 성공 시 엑세스 토큰을 응답 바디에, 리프레시 토큰과 사용자 식별자를 쿠키에 담아주는 메서드
     *
     * @param req        요청
     * @param res        응답
     * @param chain      필터체인
     * @param authResult 인증 객체
     * @author mozzi327
     */
    @Override
    @SneakyThrows
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication authResult) {
        Gson gson = new Gson();

        String email = authResult.getName();
        Member findMember = jwtTokenUtils.findMemberByEmail(email);
        String accessToken = jwtTokenUtils.generateAccessToken(findMember);
        String refreshToken = jwtTokenUtils.generateRefreshToken(findMember);
        /* ----------------- 테스트용 토큰 전용 로직 -------------------- */
        jwtTokenUtils.savedRefreshToken(refreshToken, email, findMember.getId());
        /* ----------------- 테스트용 토큰 전용 로직 -------------------- */




/*  ----------------- Redis 토큰 전용 로직 --------------------
    redisUtils.setData(refreshToken, findMember.getId(), jwtTokenUtils.getRefreshTokenExpirationMinutes());
    ----------------- Redis 토큰 전용 로직 -------------------- */

        // ***.***.*** 형식 전달을 위한 리프레시 토큰 인코딩
        String encodedRefresh = URLEncoder.encode(refreshToken, StandardCharsets.UTF_8);

        // refreshToken 쿠키 추가
        ResponseCookie refCookie = cookieUtils
                .createCookie(REFRESH_TOKEN, encodedRefresh, MAX_AGE_SECOND);

        res.setHeader(SET_COOKIE, refCookie.toString());

        // memberId 쿠키 추가
        ResponseCookie memberCookie = cookieUtils
                .createCookie(MEMBER_ID, findMember.getId().toString(), MAX_AGE_SECOND);

        res.addHeader(SET_COOKIE, memberCookie.toString());

        AuthDto.Response response = AuthDto.Response.builder()
                .accessToken(accessToken)
                .nickname(findMember.getNickname())
                .email(email)
                .build();

        res.setContentType(MediaType.APPLICATION_JSON_VALUE);
        res.getWriter().write(gson.toJson(response, AuthDto.Response.class));
        this.getSuccessHandler().onAuthenticationSuccess(req, res, authResult);
    }
}
