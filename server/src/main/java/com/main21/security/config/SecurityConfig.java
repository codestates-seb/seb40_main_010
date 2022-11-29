package com.main21.security.config;

import com.main21.member.repository.MemberRepository;
import com.main21.security.details.OAuth2DetailService;
import com.main21.security.filter.JwtVerificationFilter;
import com.main21.security.handler.MemberAccessDeniedHandler;
import com.main21.security.handler.MemberAuthenticationEntryPoint;
import com.main21.security.handler.OAuth2FailureHandler;
import com.main21.security.handler.OAuth2SuccessHandler;
import com.main21.security.provider.JwtAuthenticationProvider;
import com.main21.security.utils.JwtTokenUtils;
import com.main21.security.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CharacterEncodingFilter;
import static org.springframework.security.config.Customizer.withDefaults;
import java.util.Arrays;
import java.util.List;

/**
 * Security 설정 정보 Configuration 클래스
 * @author mozzi327
 */
@Slf4j
@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtTokenUtils jwtTokenUtils;
    private final RedisUtils redisUtils;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final OAuth2DetailService oAuth2DetailService;
    private final MemberRepository memberRepository;

    /**
     * Spring Context에 필터 체인을 등록하는 메서드
     *
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
                .cors(withDefaults())
//                .cors().disable()
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
                .accessDeniedHandler(new MemberAccessDeniedHandler())
                .and()
                .oauth2Login(oauth2 -> {
                    oauth2.userInfoEndpoint().userService(oAuth2DetailService);
                    oauth2.successHandler(new OAuth2SuccessHandler(jwtTokenUtils, memberRepository, redisUtils));
                    oauth2.failureHandler(new OAuth2FailureHandler());
                });
        return http.build();
    }

    /**
     * AllowedHeader, AllowedMethod, AllowedOrigin (sameSite 및 cors)설정을 위한 메서드
     *
     * @return CorsConfigurationSource(cors 설정 소스)
     * @author mozzi327
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedHeader("*");
        configuration.addAllowedOrigin("http://localhost:8080");
        configuration.addAllowedOrigin("https://kapi.kakao.com");
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "DELETE", "PATCH"));
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * 커스텀 Filter 클래스
     *
     * @author mozzi327
     */
    private class CustomFilterConfig extends AbstractHttpConfigurer<CustomFilterConfig, HttpSecurity> {
        @Override
        public void configure(HttpSecurity builder) {
            AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);

            JwtVerificationFilter jwtVerificationFilter
                    = new JwtVerificationFilter(authenticationManager, jwtTokenUtils, redisUtils);
            builder
                    .addFilterBefore(jwtVerificationFilter, OAuth2LoginAuthenticationFilter.class)
                    .addFilterBefore(jwtVerificationFilter, UsernamePasswordAuthenticationFilter.class)
                    .authenticationProvider(jwtAuthenticationProvider);
        }
    }
}

