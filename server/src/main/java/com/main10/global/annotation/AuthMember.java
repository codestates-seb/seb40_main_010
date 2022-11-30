package com.main10.global.annotation;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * AuthenticationPrincipal(로그인한 유저 정보) 사용을 위한 Annotation 인터페이스
 * @author mozzi327
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
//@AuthenticationPrincipal(expression = "#this == 'anonymousUser ? null : member")
@AuthenticationPrincipal
public @interface AuthMember {
}
