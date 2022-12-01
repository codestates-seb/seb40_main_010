package com.main10.global.security.resolver;

import com.main10.domain.member.dto.AuthDto;
import com.main10.global.security.dto.LoginDto;
import com.main10.global.security.token.JwtAuthenticationToken;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * authentication 정보를 사용하기 위한 Custom Method Handler Resolver(사용 미정)
 * @author mozzi327
 */
public class CustomArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return LoginDto.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        final JwtAuthenticationToken authentication
                = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new RuntimeException();
        }
        return authentication.getPrincipal();
    }
}
