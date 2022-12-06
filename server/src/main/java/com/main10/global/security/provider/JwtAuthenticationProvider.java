package com.main10.global.security.provider;

import com.main10.global.security.token.JwtAuthenticationToken;
import com.main10.global.security.utils.AuthConstants;
import com.main10.global.security.utils.CustomAuthorityUtils;
import com.main10.global.security.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * AuthenticationToken을 발급해주는 클래스
 * @author mozzi327
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {
    private final JwtTokenUtils jwtTokenUtils;
    private final CustomAuthorityUtils authorityUtils;

    /**
     * JwtAuthenticationToken 발급 메서드
     * @param authentication Authentication 정보
     * @return JwtAuthenticationToken
     * @throws AuthenticationException 인증 예외
     * @author mozzi327
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthenticationToken jwtToken = (JwtAuthenticationToken) authentication;
        Map<String, Object> claims = jwtTokenUtils
                .getClaims(jwtToken.getAccessToken());
        String email = (String) claims.get(AuthConstants.USERNAME);
        List<String> roles = (List<String>) claims.get(AuthConstants.ROLES);
        String stringId = claims.get("id").toString();
        Long id = Long.valueOf(stringId);

        List<GrantedAuthority> authorities = authorityUtils.createAuthorities(roles);

        return new JwtAuthenticationToken(authorities, email, id ,null);
    }

    /**
     * 해당 토큰이 지원하는 형식의 토큰 형식인지 판단하는 메서드
     * @param authentication Authentication 정보
     * @return boolean
     * @author mozzi327
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
