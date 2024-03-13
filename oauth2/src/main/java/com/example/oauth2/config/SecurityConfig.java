package com.example.oauth2.config;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

import com.example.oauth2.jwt.JwtAuthorizationFilter;
import com.example.oauth2.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.example.oauth2.oauth2.handler.OAuth2AuthenticationFailureHandler;
import com.example.oauth2.oauth2.handler.OAuth2AuthenticationSuccessHandler;
import com.example.oauth2.oauth2.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    private final JwtAuthorizationFilter jwtAuthorizationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
                .headers(headersConfigurer -> headersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)) // For H2 DB
                .formLogin(AbstractHttpConfigurer::disable) // Spring Security에서 기본적으로 제공되는 폼 기반 로그인을 비활성화 jwt를 사용할 경우 비활성화 디폴트
                .httpBasic(AbstractHttpConfigurer::disable) // HTTP Basic 인증을 비활성화 jwt를 사용할 경우 비활성화 디폴트
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers(antMatcher("/api/hello/**")).permitAll()//Ant 스타일 패턴은 와일드카드를 사용하여 패턴을 지정하는데, 예를 들어 /**는 임의의 하위 경로를 나타내고, *는 임의의 문자열을 의미
                        .requestMatchers(antMatcher("/h2-console/**")).permitAll() //.antMatchers("/admin/**", "/user/**").permitAll();
                        .anyRequest().authenticated() //위의 주소가 아닌 주소들 모두 권한 요청
                )
                .sessionManagement(sessions -> sessions.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //세션을 사용하지 않음 주로 Rest Api와 같이 상태를 유지할 필요 없는 서비스에서 사용, 각 요청에 인증 정보를 포함하고, 요청 간에 상태를 공유하지 않음
                .oauth2Login(configure ->
                        configure.authorizationEndpoint(config -> config.authorizationRequestRepository(httpCookieOAuth2AuthorizationRequestRepository))// OAuth 2.0 인증 프로세스 중에 사용자의 인증 요청을 관리하기 위해 서버의 쿠키를 사용
                                .userInfoEndpoint(config -> config.userService(customOAuth2UserService)) //oauth provider 서버에서 받아올 사용자의 정보 , 이메일 나이 등
                                .successHandler(oAuth2AuthenticationSuccessHandler)
                                .failureHandler(oAuth2AuthenticationFailureHandler)
                );

        http.addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
