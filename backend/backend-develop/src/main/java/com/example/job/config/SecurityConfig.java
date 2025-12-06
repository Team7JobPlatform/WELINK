package com.example.job.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration          // 보안 관련 설정을 담는 스프링 설정 클래스
@EnableWebSecurity      // Spring Security 활성화
public class SecurityConfig {

    // JWT 생성·검증을 위한 유틸 빈 주입
    private final JwtTokenProvider jwtTokenProvider;

    public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // 비밀번호 암호화를 위한 BCryptPasswordEncoder 빈 등록
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // HTTP 보안 설정 핵심 부분
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // CSRF 토큰 비활성화 (REST API + JWT 방식이라 필요 없음)
                .csrf(csrf -> csrf.disable())
                // 세션을 사용하지 않고, 매 요청마다 토큰으로만 인증 → STATELESS
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // 요청별 인가 규칙 설정
                .authorizeHttpRequests(auth -> auth
                        // 회원가입/로그인은 인증 없이 누구나 접근 가능
                        .requestMatchers(
                                "/api/users/signup",
                                "/api/users/login"
                        ).permitAll()
                        // 위에서 지정한 경로 외 나머지는 모두 인증(로그인) 필수
                        .anyRequest().authenticated()
                )
                // UsernamePasswordAuthenticationFilter 전에 JWT 인증 필터를 추가
                .addFilterBefore(
                        new JwtAuthenticationFilter(jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class
                );

        // 설정을 마친 후 SecurityFilterChain 객체 반환
        return http.build();
    }
}
