package com.main21.utils;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.List;

public class WithMockCustomMemberSercurityContextFactory implements WithSecurityContextFactory<WithMockCustomMember> {
    @Override
    public SecurityContext createSecurityContext(WithMockCustomMember annotation) {
        UsernamePasswordAuthenticationToken member
                = new UsernamePasswordAuthenticationToken(annotation.username(), null, List.of(new SimpleGrantedAuthority(annotation.role())));
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(member);

        return context;
    }
}
