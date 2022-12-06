package com.main10.global.security.config;

import com.main10.domain.member.repository.MemberRepository;
import com.main10.global.security.handler.MemberAccessDeniedHandler;
import com.main10.global.security.handler.MemberAuthenticationEntryPoint;
import com.main10.global.security.utils.RedisUtils;
import com.main10.global.security.details.OAuth2DetailService;
import com.main10.global.security.filter.JwtVerificationFilter;
import com.main10.global.security.handler.OAuth2FailureHandler;
import com.main10.global.security.handler.OAuth2SuccessHandler;
import com.main10.global.security.provider.JwtAuthenticationProvider;
import com.main10.global.security.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
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
import java.util.List;

import static com.main10.global.security.utils.AuthConstants.AUTHORIZATION;
import static com.main10.global.security.utils.AuthConstants.REFRESH_TOKEN;

/**
 * Security 설정 정보 Configuration 클래스
 * @author mozzi327
 */
@Slf4j
@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfig {

    @Value("${address.front-local}")
    private final String FRONT_LOCAL;

    @Value("${address.front-server}")
    private final String FRONT_SERVER;

    @Value("${address.front-server-https}")
    private final String FRONT_SERVER_HTTPS;

    @Value("${address.domain}")
    private final String DOMAIN;

    @Value("${address.local}")
    private final String LOCAL;

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
                .csrf().disable()
                .cors().configurationSource(corsConfigurationSource())
                .and()
                .httpBasic().disable()
                .formLogin().disable()
                .apply(new CustomFilterConfig())
                .and()
                .authorizeHttpRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                /* --------------------------------------------     기타      -----------------------------------------*/
                .antMatchers(HttpMethod.GET, "/h2/**").hasAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.GET, "/docs/**").hasAuthority("ROLE_ADMIN")
                /* --------------------------------------------  AUTH 도메인  -----------------------------------------*/
                .antMatchers(HttpMethod.POST, "/auth/login").permitAll()
                .antMatchers(HttpMethod.DELETE, "/auth/logout").hasAuthority("ROLE_USER")
                .antMatchers(HttpMethod.GET, "/auth/re-issue").hasAuthority("ROLE_USER")
                /* -------------------------------------------- MEMBER 도메인 -----------------------------------------*/
                .antMatchers(HttpMethod.GET, "/member").hasAuthority("ROLE_USER")
//                .antMatchers(HttpMethod.POST, "/member/join").permitAll()
                .antMatchers(HttpMethod.PATCH, "/member/edit").hasAuthority("ROLE_USER")
                .antMatchers(HttpMethod.PATCH, "/member/profile").hasAuthority("ROLE_USER")
                .antMatchers(HttpMethod.PATCH, "/member/profileLocal").hasAuthority("ROLE_USER")
                /* -------------------------------------------- BOOKMARK 도메인 -----------------------------------------*/
                .antMatchers(HttpMethod.GET, "/bookmark/*").hasAuthority("ROLE_USER")
                .antMatchers(HttpMethod.GET, "/bookmark").hasAuthority("ROLE_USER")
                /* --------------------------------------------  PLACE 도메인  -----------------------------------------*/
                // - place
                .antMatchers(HttpMethod.GET, "/place/*").permitAll()
                .antMatchers(HttpMethod.GET, "/place").hasAuthority("ROLE_USER")
                .antMatchers(HttpMethod.DELETE, "/place/*").hasAuthority("ROLE_USER")
                .antMatchers(HttpMethod.POST, "/place/post").hasAuthority("ROLE_USER")
                .antMatchers(HttpMethod.POST, "/place/*/edit").hasAuthority("ROLE_USER")
                .antMatchers(HttpMethod.GET, "/home").permitAll()
                .antMatchers(HttpMethod.GET, "/home/*").permitAll()
                // -search
                .antMatchers(HttpMethod.GET, "/category/**").permitAll()
                .antMatchers(HttpMethod.GET, "/search/*").permitAll()
                .antMatchers(HttpMethod.POST, "/search/detail").permitAll()
                /* -------------------------------------------- RESERVE 도메인 -----------------------------------------*/
                .antMatchers(HttpMethod.GET, "/reserve").hasAuthority("ROLE_USER")
                .antMatchers(HttpMethod.DELETE, "/reserve/*").hasAuthority("ROLE_USER")
                .antMatchers(HttpMethod.GET, "/api/reserve/*/cancel").permitAll()
                .antMatchers(HttpMethod.GET, "/api/reserve/*/fail").permitAll()
                .antMatchers(HttpMethod.GET, "/api/reserve/*/completed").permitAll()
                .antMatchers(HttpMethod.POST, "/place/*/reserve").hasAuthority("ROLE_USER")
                .antMatchers(HttpMethod.GET, "/place/reserve/*/payment").hasAuthority("ROLE_USER")
                .antMatchers(HttpMethod.GET, "/place/reserve/*/edit").hasAuthority("ROLE_USER")
                /* -------------------------------------------- REVIEW 도메인 -----------------------------------------*/
                .antMatchers(HttpMethod.POST, "/review/place/*/reserve/*").hasAuthority("ROLE_USER")
                .antMatchers(HttpMethod.PATCH, "/review/*/edit").hasAuthority("ROLE_USER")
                .antMatchers(HttpMethod.DELETE, "/review/*").hasAuthority("ROLE_USER")
                .antMatchers(HttpMethod.GET, "/review").hasAuthority("ROLE_USER")
                .antMatchers(HttpMethod.GET, "/review/place/*").permitAll()
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
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin(FRONT_LOCAL);
        configuration.addAllowedOrigin(FRONT_SERVER);
        configuration.addAllowedOrigin(FRONT_SERVER_HTTPS);
        configuration.addAllowedOrigin(DOMAIN);
        configuration.addAllowedOrigin(LOCAL);
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);
        configuration.addExposedHeader(AUTHORIZATION);
        configuration.addExposedHeader(REFRESH_TOKEN);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
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

