package com.main21.security.config;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

public class CustomFilterConfig extends AbstractHttpConfigurer<CustomFilterConfig, HttpSecurity> {
    @Override
    public void configure(HttpSecurity builder) {
//        AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);
//
//        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager, jwtTokenizer);
//        jwtAuthenticationFilter.setFilterProcessesUrl("/users/login");
//        jwtAuthenticationFilter.setAuthenticationSuccessHandler(new UserAuthenticationSuccessHandler());
//        jwtAuthenticationFilter.setAuthenticationFailureHandler(new UserAuthenticationFailureHandler());
//
//        JwtVerificationFilter jwtVerificationFilter = new JwtVerificationFilter(jwtTokenizer, authorityUtils);
//
//        builder
//                .addFilter(jwtAuthenticationFilter)
//                .addFilterAfter(jwtVerificationFilter, JwtAuthenticationFilter.class);
    }

}
