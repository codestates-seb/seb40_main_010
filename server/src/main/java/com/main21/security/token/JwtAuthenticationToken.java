package com.main21.security.token;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {
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

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
