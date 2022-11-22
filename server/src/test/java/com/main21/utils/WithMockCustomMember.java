package com.main21.utils;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomMemberSercurityContextFactory.class)
public @interface WithMockCustomMember {
    String username() default "hgd@gmail.com";
    String role() default "ROLE_USER";
}
