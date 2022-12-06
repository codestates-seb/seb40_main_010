package com.main10.global.security.handler;

import com.main10.global.exception.ExceptionCode;
import com.main10.domain.member.entity.AccountStatus;
import com.main10.domain.member.entity.Member;
import com.main10.domain.member.repository.MemberRepository;
import com.main10.global.security.exception.AuthException;
import com.main10.global.security.utils.AuthConstants;
import com.main10.global.security.utils.JwtTokenUtils;
import com.main10.global.security.utils.RedisUtils;
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
import org.springframework.util.ObjectUtils;
import org.springframework.web.util.UriComponentsBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.Map;
import java.util.Objects;

import static com.main10.global.security.utils.AuthConstants.*;

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
        if (AuthConstants.KAKAO.equals(provider)) {
            Map<String, Object> kakaoAccount = (Map<String, Object>) oAuth2.getAttributes().get(AuthConstants.KAKAO_ACCOUNT);
            email = String.valueOf(kakaoAccount.get(AuthConstants.EMAIL));
        } else email = (String) oAuth2.getAttributes().get(AuthConstants.EMAIL);

        Member member = memberRepository
                .findMemberByEmailAndAccountStatus(email, AccountStatus.whatIsProvider(provider))
                .orElseThrow(() -> new AuthException(ExceptionCode.MEMBER_NOT_FOUND));

        String accessToken = jwtTokenUtils.generateAccessToken(member);
        String refreshToken = jwtTokenUtils.generateRefreshToken(member);

        if (!Objects.isNull(redisUtils.getData(member.getEmail(), provider)))
            redisUtils.deleteData(member.getEmail(), provider);

        redisUtils.setData(email, provider ,refreshToken, jwtTokenUtils.getRefreshTokenExpirationMinutes());
        String responseUrl = createUri(accessToken, refreshToken, provider, member.getMbti()).toString();

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
                          String provider,
                          String mbti) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add(AUTHORIZATION, accessToken);
        queryParams.add(REFRESH_TOKEN, refreshToken);
        queryParams.add(MBTI, mbti);

        return UriComponentsBuilder
                .newInstance()
                .scheme("https")
                .host("daeyeo4u.com")
                .path("/oauth/" + provider)
                .queryParams(queryParams)
                .build()
                .toUri();
    }
}
