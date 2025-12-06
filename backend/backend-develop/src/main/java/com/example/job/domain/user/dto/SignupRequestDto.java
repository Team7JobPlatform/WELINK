package com.example.job.domain.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter                         // 각 필드에 대한 getter 메서드 생성
@Setter                         // 각 필드에 대한 setter 메서드 생성
public class SignupRequestDto {

    // 회원가입 시 사용할 이메일
    private String email;

    // 회원가입 시 설정할 비밀번호
    private String password;

    // 사용자 이름
    private String name;
}
