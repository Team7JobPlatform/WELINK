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
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer; // 현대적 DSL을 위해 필요

@Configuration
@EnableWebSecurity
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
                .csrf(AbstractHttpConfigurer::disable)
                // 기본 인증 비활성화
                .httpBasic(AbstractHttpConfigurer::disable)
                // 세션을 사용하지 않고, 매 요청마다 토큰으로만 인증 → STATELESS
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // 요청별 인가 규칙 설정
                .authorizeHttpRequests(auth -> auth
                        // 1. PUBLIC PATHS (인증 없이 접근 가능)
                        .requestMatchers(
                                // 로그인/회원가입 API
                                "/api/users/signup",
                                "/api/users/login",
                                // 공개 데이터 API 및 프론트엔드 파일 경로
                                "/api/welfare-items/**",
                                "/api/companies/**",
                                "/api/jobs/all"
                        ).permitAll()
                        // 2. 위에서 지정한 경로 외 나머지는 모두 인증(로그인) 필수
                        .anyRequest().authenticated()
                )
                // ★★★ 커스텀 JWT 인증 필터 추가 (토큰 검사) ★★★
                .addFilterBefore(
                        new JwtAuthenticationFilter(jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class // 일반 로그인 필터보다 먼저 실행
                );

        // 설정을 마친 후 SecurityFilterChain 객체 반환
        return http.build();
    }
}