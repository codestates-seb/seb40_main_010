package com.main21.security.handler;

import com.main21.exception.ExceptionCode;
import com.main21.member.entity.AccountStatus;
import com.main21.member.entity.Member;
import com.main21.member.repository.MemberRepository;
import com.main21.security.exception.AuthException;
import com.main21.security.utils.JwtTokenUtils;
import com.main21.security.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.Map;

import static com.main21.security.utils.AuthConstants.*;

/**
 * OAuth2 로그인 성공 핸들러 클래스
 * @author mozzi327
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final JwtTokenUtils jwtTokenUtils;
    private final MemberRepository memberRepository;
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    private final RedisUtils redisUtils;

    /**
     * 로그인 성공 헨들러 메서드
     * @param req 요청 정보
     * @param res 응답 정보
     * @param authentication 생성된 Authentication 정보
     * @author mozzi327
     */
    @Override
    @SneakyThrows
    public void onAuthenticationSuccess(HttpServletRequest req,
                                        HttpServletResponse res,
                                        Authentication authentication) {
        log.info("로그인 성공 시작");

        OAuth2AuthenticationToken forProviderInfo = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2 = (OAuth2User) authentication.getPrincipal();
        String provider = forProviderInfo.getAuthorizedClientRegistrationId();

        String email;
        /*- 카카오만 달라.. -*/
        if (KAKAO.equals(provider)) {
            Map<String, Object> kakaoAccount = (Map<String, Object>) oAuth2.getAttributes().get(KAKAO_ACCOUNT);
            email = String.valueOf(kakaoAccount.get(EMAIL));
        } else email = (String) oAuth2.getAttributes().get(EMAIL);

        Member member = memberRepository
                .findMemberByEmailAndAccountStatus(email, AccountStatus.whatIsProvider(provider))
                .orElseThrow(() -> new AuthException(ExceptionCode.MEMBER_NOT_FOUND));

        String accessToken = jwtTokenUtils.generateAccessToken(member);
        String refreshToken = jwtTokenUtils.generateRefreshToken(member);

        redisUtils.setData(refreshToken, member.getId(), jwtTokenUtils.getRefreshTokenExpirationMinutes());
        String responseUrl = createUri(accessToken, refreshToken, provider).toString();

        redirectStrategy.sendRedirect(req, res, responseUrl);

        log.info("로그인 성공 종료");
    }

    /**
     * URI 생성 메서드
     * @param accessToken 액세스 토큰
     * @param refreshToken 리프레시 토큰
     * @param provider OAuth 인증 공급자
     * @return URI(리다이렉트 URI)
     * @author mozzi327
     */
    private URI createUri(String accessToken,
                          String refreshToken,
                          String provider) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add(AUTHORIZATION, accessToken);
        queryParams.add(REFRESH_TOKEN, refreshToken);

        return UriComponentsBuilder
                .newInstance()
                .scheme("http")
                .host("localhost")
                .port(8080)
                .path("/oauth/" + provider)
                .queryParams(queryParams)
                .build()
                .toUri();
    }
}
