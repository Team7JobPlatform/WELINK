package com.example.job.domain.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter                     // 각 필드에 대한 getter 메서드 자동 생성
@Setter                     // 각 필드에 대한 setter 메서드 자동 생성
public class LoginRequestDto {

    // 로그인 시 사용하는 이메일
    private String email;

    // 로그인 시 사용하는 비밀번호
    private String password;
}
