package com.example.oauthjwt.config;

import com.example.oauthjwt.jwt.CustomLogoutFilter;
import com.example.oauthjwt.jwt.JWTFilter;
import com.example.oauthjwt.jwt.JWTUtil;
import com.example.oauthjwt.jwt.TokenRepository;
import com.example.oauthjwt.oauth2.CustomSuccessHandler;
import com.example.oauthjwt.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.example.oauthjwt.service.CustomOAuth2UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomSuccessHandler customSuccessHandler;
    private final JWTUtil jwtUtil;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    private final TokenRepository tokenRepository;

    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService, CustomSuccessHandler customSuccessHandler, JWTUtil jwtUtil, HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository, TokenRepository tokenRepository) {

        this.customOAuth2UserService = customOAuth2UserService;
        this.customSuccessHandler = customSuccessHandler;
        this.jwtUtil = jwtUtil;
        this.httpCookieOAuth2AuthorizationRequestRepository = httpCookieOAuth2AuthorizationRequestRepository;
        this.tokenRepository = tokenRepository;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        http
                .cors(AbstractHttpConfigurer::disable);

        //csrf disable, jwt 방식을 사용하기 때문에 꺼도됨
        http
                .csrf(AbstractHttpConfigurer::disable);

        //From 로그인 방식 disable, oauth를 통해 로그인을 진행하기 때문에 꺼도됨
        http
                .formLogin(AbstractHttpConfigurer::disable);

        //HTTP Basic 인증 방식 disable, oauth를 통해 로그인을 진행하기 때문에 꺼도됨
        http
                .httpBasic(AbstractHttpConfigurer::disable);
        //JWTFilter 추가
        //JWTFilter를 UsernamePasswordAuthenticationFilter이전에 등록
        http
                .addFilterAfter(new JWTFilter(jwtUtil), OAuth2LoginAuthenticationFilter.class)
                .addFilterBefore(new CustomLogoutFilter(jwtUtil,tokenRepository), LogoutFilter.class);

        //oauth2
        http
                .oauth2Login((oauth2) -> oauth2
                        .authorizationEndpoint(config -> config.authorizationRequestRepository(httpCookieOAuth2AuthorizationRequestRepository))
                        .userInfoEndpoint((userInfoEndpointConfig -> userInfoEndpointConfig
                                .userService(customOAuth2UserService)))
                        .successHandler(customSuccessHandler));

        //경로별 인가 작업, 루트 경로는 허용, 나머지 경로는 인증 필요
        http
                .authorizeHttpRequests((auth) -> auth
//                        .requestMatchers("/").permitAll()
                        .requestMatchers("/reissue").permitAll()
                        .anyRequest().authenticated());

        //세션 설정 : STATELESS, jwt를 이용하기 때문에 세션은 사용하지 않음
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}