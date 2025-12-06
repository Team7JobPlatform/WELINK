package com.example.job.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter                         // 응답 필드에 대한 getter 메서드 자동 생성
@AllArgsConstructor            // 모든 필드를 받는 생성자 자동 생성
public class LoginResponseDto {

    // 로그인한 사용자의 고유 ID
    private Long userId;

    // 로그인한 사용자의 이름
    private String name;

    // 인증에 사용할 JWT 액세스 토큰
    private String token;
}
