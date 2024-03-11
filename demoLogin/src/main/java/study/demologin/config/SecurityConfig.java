package study.demologin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()  // csrf 보안 설정 여부
                .authorizeRequests()  // url에 대한 인증 설정
                .antMatchers("/user/**").authenticated()  // 인증되어야만 url 정상 처리
                .antMatchers("/manager/**").access("hasRole('MANAGER') or hasRole('ADMIN')")  // 특정 권한이 있어야만 url 정상 처리
                .antMatchers("/admin/**").access("hasRole('ADMIN')")
                .anyRequest().permitAll()  // 그외는 인증을 요구하지 않음
                .and()
                .formLogin();  // spring security에서 제공하는 로그인 form을 사용함
        return http.build();
    }
}
