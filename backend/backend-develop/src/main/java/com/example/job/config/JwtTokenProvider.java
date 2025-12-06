package com.example.job.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component  // 스프링이 관리하는 빈으로 등록해서 어디서든 주입 받아 사용
public class JwtTokenProvider {

    // HS256 알고리즘으로 생성한 비밀키 (서버 내부에서만 사용)
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // 액세스 토큰 유효 시간: 1시간(밀리초 기준)
    private final long validityInMs = 60 * 60 * 1000L;

    // 로그인 성공 시 JWT 생성
    public String createToken(Long userId, String email, String role) {
        Date now = new Date();                             // 현재 시각
        Date expiry = new Date(now.getTime() + validityInMs); // 만료 시각

        return Jwts.builder()
                .setSubject(String.valueOf(userId)) // 토큰 주제: 사용자 ID
                .setIssuedAt(now)                   // 발급 시각
                .setExpiration(expiry)              // 만료 시각
                .claim("email", email)              // 추가 클레임: 이메일
                .claim("role", role)                // 추가 클레임: 권한(ROLE_USER 등)
                .signWith(key)                      // 비밀키로 서명
                .compact();                         // 최종 문자열(JWT)로 변환
    }

    // 이미 발급된 토큰에서 userId(subject)만 추출
    public Long getUserIdFromToken(String token) {
        return Long.parseLong(
                Jwts.parserBuilder()
                        .setSigningKey(key)              // 같은 비밀키로 서명 검증
                        .build()
                        .parseClaimsJws(token)           // 토큰 파싱 + 검증
                        .getBody()
                        .getSubject()                    // subject(userId) 가져오기
        );
    }

    // JwtAuthenticationFilter 등에서 서명 검증에 사용하기 위한 Key getter
    public Key getKey() {
        return key;
    }
}
