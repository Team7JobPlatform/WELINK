package com.example.job.config;

// 핵심 Security Imports: javax.servlet 표준 사용
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.util.StringUtils;
import io.jsonwebtoken.JwtException;

import java.io.IOException;
import java.util.List;

// 매 요청마다 한 번만 실행되는 JWT 인증 필터
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // 실제 필터 로직: 모든 API 요청마다 호출됨
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Authorization 헤더에서 토큰 추출 (JwtTokenProvider 유틸리티 사용)
        String token = jwtTokenProvider.resolveToken(request);

        // 2. 토큰이 존재하고 유효한 경우에만 인증 처리
        if (token != null && jwtTokenProvider.validateToken(token)) {
            try {
                // 토큰에서 사용자 ID 추출 (principal로 사용)
                Long userId = jwtTokenProvider.getUserIdFromToken(token);

                // Spring Security의 Authentication 객체 생성 (권한은 ROLE_USER로 가정)
                Authentication authentication =
                        new UsernamePasswordAuthenticationToken(
                                String.valueOf(userId), // principal: 로그인 사용자 ID (String)
                                null,                    // credentials: 비밀번호는 JWT 방식에서 사용 안 함
                                List.of(new SimpleGrantedAuthority("ROLE_USER")) // 권한 부여
                        );

                // 현재 요청의 SecurityContext 에 인증 정보 저장 (로그인 상태 설정)
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception e) {
                // 토큰 만료, 변조 등 유효하지 않은 토큰 처리
                // Security Context를 비워서 인증 실패 상태로 만듦
                SecurityContextHolder.clearContext();
                // 401 Unauthorized 에러는 SecurityFilterChain의 이후 단계에서 처리됩니다.
            }
        }

        // 다음 필터 또는 서블릿으로 요청 전달
        filterChain.doFilter(request, response);
    }
}