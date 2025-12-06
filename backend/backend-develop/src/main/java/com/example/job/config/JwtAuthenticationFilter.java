package com.example.job.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

// 매 요청마다 한 번만 실행되는 JWT 인증 필터
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // JWT 생성·검증에 사용하는 유틸 클래스
    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // 실제 필터 로직: 요청마다 호출됨
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        System.out.println(">>> path = " + path); // 현재 요청 경로 로그

        // 회원가입/로그인 요청은 JWT 검사 없이 통과
        if (path.startsWith("/api/users/signup") ||
                path.startsWith("/api/users/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Authorization 헤더에서 Bearer 토큰 추출
        String token = resolveToken(request);
        System.out.println(">>> token = " + token); // 토큰 값 로그

        // 토큰이 있으면 검증 및 인증 정보 세팅 시도
        if (token != null) {
            try {
                // 토큰 파싱 및 서명 검증
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(jwtTokenProvider.getKey())
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                // 토큰에 저장된 사용자 ID(subject)와 권한(role) 추출
                String userId = claims.getSubject();
                String role = claims.get("role", String.class);
                System.out.println("userId=" + userId + ", role=" + role);  // 파싱 결과 로그

                // 추출한 정보로 Spring Security Authentication 객체 생성
                Authentication auth =
                        new UsernamePasswordAuthenticationToken(
                                userId,                         // principal: 로그인 사용자 식별자
                                null,                           // credentials: 비밀번호는 필요 없음
                                List.of(new SimpleGrantedAuthority(role)) // 권한 목록
                        );

                // 현재 요청의 SecurityContext 에 인증 정보 저장
                SecurityContextHolder.getContext().setAuthentication(auth);

            } catch (Exception e) {
                e.printStackTrace(); // 토큰 관련 예외 로그
                // 토큰이 유효하지 않으면 인증 정보를 비우고, 이후 단계에서 401 처리
                SecurityContextHolder.clearContext();
            }
        }

        // 다음 필터로 요청 전달
        filterChain.doFilter(request, response);
    }

    // Authorization 헤더에서 Bearer 토큰 문자열만 잘라내는 헬퍼 메서드
    private String resolveToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            return header.substring(7); // "Bearer " 이후 실제 토큰 부분만 반환
        }
        return null;
    }
}
