/*
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
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.web.cors.CorsConfiguration; // ★ CORS 관련 import 추가
import java.util.Arrays; // ★ Arrays import 추가

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // ★★★ [수정 핵심] CORS 설정을 명시적으로 추가하여 5500 포트 허용 ★★★
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    // Live Server (5500)와 127.0.0.1 출처 허용
                    config.setAllowedOrigins(Arrays.asList("http://localhost:5500", "http://127.0.0.1:5500"));
                    config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    config.setAllowedHeaders(Arrays.asList("*")); // 모든 헤더 허용 (Authorization 헤더 포함)
                    config.setAllowCredentials(true); // 인증 정보 (토큰) 허용
                    return config;
                }))
                // CSRF 토큰 비활성화
                .csrf(AbstractHttpConfigurer::disable)
                // 세션을 사용하지 않고 토큰으로 인증
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // 요청별 인가 규칙 설정 (기존 PUBLIC PATHs 유지)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/users/signup", "/api/users/login",
                                "/api/welfare-items/**", "/api/companies/**", "/api/jobs/all"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                // JWT 인증 필터 추가
                .addFilterBefore(
                        new JwtAuthenticationFilter(jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}*/
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
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.web.cors.CorsConfiguration;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // ★★★ CORS 설정 (5500 포트 허용) ★★★
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(Arrays.asList("http://localhost:5500", "http://127.0.0.1:5500"));
                    config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    config.setAllowedHeaders(Arrays.asList("*"));
                    config.setAllowCredentials(true);
                    return config;
                }))
                // CSRF 비활성화
                .csrf(AbstractHttpConfigurer::disable)
                // 세션 비활성화
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // 요청별 인가 규칙 설정
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/users/signup", "/api/users/login",
                                "/api/welfare-items/**", "/api/companies/**", "/api/jobs/all"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                // ★★★ 커스텀 JWT 인증 필터 추가 ★★★
                .addFilterBefore(
                        new JwtAuthenticationFilter(jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}