package com.main21.security.provider;

import com.main21.security.token.JwtAuthenticationToken;
import com.main21.security.utils.CustomAuthorityUtils;
import com.main21.security.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static com.main21.security.utils.AuthConstants.*;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {
    private final JwtTokenUtils jwtTokenUtils;
    private final CustomAuthorityUtils authorityUtils;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Map<String, Object> claims = jwtTokenUtils
                .getClaims(String.valueOf(((JwtAuthenticationToken) authentication)
                .getAccessToken()));
        String email = (String) claims.get(USERNAME);
        List<String> roles = (List<String>) claims.get(ROLES);

        List<GrantedAuthority> authorities = authorityUtils.createAuthorities(roles);

        return new JwtAuthenticationToken(authorities, email, null);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
