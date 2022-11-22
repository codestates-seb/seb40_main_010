package com.main21.security.config;

import com.main21.security.filter.JwtVerificationFilter;
import com.main21.security.handler.MemberAccessDeniedHandler;
import com.main21.security.handler.MemberAuthenticationEntryPoint;
import com.main21.security.provider.JwtAuthenticationProvider;
import com.main21.security.utils.JwtTokenUtils;
import com.main21.security.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtTokenUtils jwtTokenUtils;
    private final RedisUtils redisUtils;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;


    /**
     * Spring Context에 필터 체인을 등록하는 메서드
     * @param http HttpSecurity
     * @return SecurityFilterChain
     * @author mozzi327
     */
    @Bean
    @SneakyThrows
    public SecurityFilterChain filterChain(HttpSecurity http) {
        CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
        encodingFilter.setEncoding("UTF-8");
        encodingFilter.setForceEncoding(true);

        http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(encodingFilter, CsrfFilter.class)
                .headers().frameOptions().disable()
                .and()
//                .cors(withDefaults())
                .cors().disable()
                .csrf().disable()
                .httpBasic().disable()
                .formLogin().disable()
                .apply(new CustomFilterConfig())
                .and()
                .authorizeHttpRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .antMatchers(HttpMethod.POST, "/member/join").permitAll()
                .antMatchers(HttpMethod.GET, "/h2/**").permitAll()
                .anyRequest().permitAll()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new MemberAuthenticationEntryPoint())
                .accessDeniedHandler(new MemberAccessDeniedHandler());

        return http.build();
    }



    /**
     * AllowedHeader, AllowedMethod, AllowedOrigin (sameSite 및 cors)설정을 위한 메서드
     * @return CorsConfigurationSource(cors 설정 소스)
     * @author mozzi327
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.addAllowedHeader("*");
//        configuration.addAllowedMethod("*");
//        configuration.addAllowedOriginPattern("*");
//        configuration.addAllowedOrigin("https://api.mozzidev.com");
//        configuration.addAllowedOrigin("https://d184hsf03uyfp2.cloudfront.net");
//        configuration.addAllowedOrigin("http://localhost:8080");
//        configuration.setAllowCredentials(true);
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "DELETE", "PATCH"));
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }


    /**
     * 커스텀 Filter 클래스
     * @author mozzi327
     */
    private class CustomFilterConfig extends AbstractHttpConfigurer<CustomFilterConfig, HttpSecurity> {
        @Override
        public void configure(HttpSecurity builder) {
            AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);

            JwtVerificationFilter jwtVerificationFilter
                    = new JwtVerificationFilter(authenticationManager, jwtTokenUtils, redisUtils);

            SimpleUrlAuthenticationSuccessHandler successHandler = new SimpleUrlAuthenticationSuccessHandler();

            builder
                    .addFilterBefore(jwtVerificationFilter, UsernamePasswordAuthenticationFilter.class)
                    .authenticationProvider(jwtAuthenticationProvider);
        }
    }
}

