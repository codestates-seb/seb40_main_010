package com.main21.security.filter;

import com.main21.exception.ExceptionCode;
import com.main21.security.exception.AuthException;
import com.main21.security.utils.CookieUtils;
import com.main21.security.utils.CustomAuthorityUtils;
import com.main21.security.utils.JwtTokenUtils;
import com.main21.security.utils.RedisUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Map;

import static com.main21.security.utils.AuthConstants.*;

@Slf4j
@RequiredArgsConstructor
public class JwtVerificationFilter extends OncePerRequestFilter {
    private final JwtTokenUtils jwtTokenUtils;
    private final CustomAuthorityUtils authorityUtils;
    private final CookieUtils cookieUtils;
    private final RedisUtils redisUtils;


    /**
     * 사용자 요청에 대한 권한 인증 메서드<br>
     * - 쿠키에 저장된 리프레시 토큰을 가져와 데이터베이스에 리프레시 토큰이 존재하는지 확인하고,<br>
     * 액세스 토큰이 유효한지 확인한다.
     *
     * @param req         요청
     * @param res         응답
     * @param filterChain 필터 체인
     * @author mozzi327
     */
    @Override
    @SneakyThrows
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain filterChain) {
// ----------------- Redis 토큰 전용 로직 --------------------

/*        String accessToken = req.getHeader(AUTHORIZATION);
        if (accessToken != null && jwtTokenUtils.validateToken(accessToken)) {
            String isLogout = (String) redisUtils.getData(accessToken);
            if (ObjectUtils.isEmpty(isLogout)) {
                Map<String, Object> claims = verifyJws(req);
                setAuthenticationToContext(claims);
            }
        }

        filterChain.doFilter(req, res);*/
//   ----------------- Redis 토큰 전용 로직 --------------------

        try {
            String refreshToken = cookieUtils.isExistRefresh(req.getCookies());
            jwtTokenUtils.verifiedExistRefresh(refreshToken);
            Map<String, Object> claims = verifyJws(req);
            setAuthenticationToContext(claims);
        } catch (SignatureException se) {
            req.setAttribute("exception", se);
        } catch (ExpiredJwtException ee) {
            req.setAttribute("exception", ee);
        } catch (Exception e) {
            req.setAttribute("exception", e);
        }

        filterChain.doFilter(req, res);

    }


    /**
     * 헤더에 엑세스 토큰이 존재하는지 유무를 확인하는 메서드
     *
     * @param req 요청
     * @return boolean(액세스 토큰 유무 확인)
     * @author mozzi327
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest req) {
        String authentication = req.getHeader(AUTHORIZATION);
        return authentication == null || !authentication.startsWith(BEARER);
    }


    /**
     * 요청에서 claims 정보를 추출하는 메서드
     *
     * @param req 요청
     * @return Map(String, Object) - claims 정보
     * @author mozzi327
     */
    private Map<String, Object> verifyJws(HttpServletRequest req) {
        String jws = req
                .getHeader(AUTHORIZATION)
                .replace(BEARER, "");
        String base64EncodedSecretKey = jwtTokenUtils
                .encodeBase64SecretKey(jwtTokenUtils.getSecretKey());
        return jwtTokenUtils
                .getClaims(jws, base64EncodedSecretKey)
                .getBody();
    }


    /**
     * 추출한 claims 정보를 SecurityContextHolder context에 등록하는 메서드
     *
     * @param claims claims 정보
     * @author mozzi327
     */
    private void setAuthenticationToContext(Map<String, Object> claims) {
        String email = (String) claims.get(USERNAME);
        List<GrantedAuthority> authorities = authorityUtils.createAuthorities(email);
        Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
