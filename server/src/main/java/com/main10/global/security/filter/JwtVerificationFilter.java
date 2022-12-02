package com.main10.global.security.filter;

import com.main10.global.security.token.JwtAuthenticationToken;
import com.main10.global.security.utils.AuthConstants;
import com.main10.global.security.utils.RedisUtils;
import com.main10.global.security.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.main10.global.security.utils.AuthConstants.BEARER;

/**
 * 헤더에 실린 토큰 정보를 통해 요청에 대한 인가를 처리해주는 클래스
 * @author mozzi327
 */
@Slf4j
@RequiredArgsConstructor
public class JwtVerificationFilter extends OncePerRequestFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtils jwtTokenUtils;
    private final RedisUtils redisUtils;

    /**
     * 사용자 요청에 대한 권한 인증 메서드<br>
     * - 헤더에 저장된 리프레시 토큰을 가져와 레디스에 리프레시 토큰이 존재하는지 확인하고,<br>
     * 액세스 토큰이 유효한지 확인한다.
     *
     * @param req         요청
     * @param res         응답
     * @param filterChain 필터 체인
     * @author mozzi327
     */
    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain filterChain) throws ServletException, IOException {

        String accessToken = req.getHeader(AuthConstants.AUTHORIZATION);

        if (StringUtils.hasText(accessToken) && accessToken.startsWith(BEARER)) {
            accessToken = jwtTokenUtils.parseAccessToken(accessToken);
            String isLogout = redisUtils.isBlackList(accessToken);
            if (isLogout == null) { // 유효한 토큰일 경우
                Authentication authentication = new JwtAuthenticationToken(accessToken);
                Authentication authenticated = authenticationManager.authenticate(authentication);
                setAuthenticationToContext(authenticated);
            }
        }

        doFilter(req, res, filterChain);
    }

    /**
     * 추출한 claims 정보를 SecurityContextHolder context에 등록하는 메서드
     *
     * @param authentication 권한 정보
     * @author mozzi327
     */
    private void setAuthenticationToContext(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
