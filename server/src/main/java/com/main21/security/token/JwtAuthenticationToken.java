package com.main21.security.token;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Authentication Token 커스텀 클래스
 * @author mozzi327
 */
public class JwtAuthenticationToken extends AbstractAuthenticationToken {
    private Long memberId;
    private String accessToken;
    private Object principal;
    private Object credentials;

    public JwtAuthenticationToken(Collection<? extends GrantedAuthority> authorities,
                                  Object principal,
                                  Object credentials) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
    }

    public JwtAuthenticationToken(String accessToken) {
        super(null);
        this.accessToken = accessToken;
        this.setAuthenticated(false);
    }

    /**
     * Credential 정보 리턴 메서드
     * @return credentials
     * @author mozzi327
     */
    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    /**
     * Principal 정보 리턴 메서드
     * @return principal(User or OAuth2User)
     * @author mozzi327
     */
    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    /**
     * AccessToken 리턴 메서드
     * @return accessToken
     * @author mozzi327
     */
    public String getAccessToken() {
        return accessToken;
    }
}
