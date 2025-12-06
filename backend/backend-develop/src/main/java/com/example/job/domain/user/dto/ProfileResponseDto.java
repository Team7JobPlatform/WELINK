package com.example.job.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter                               // 각 필드에 대한 getter 메서드 자동 생성
@AllArgsConstructor                  // 모든 필드를 받는 생성자 자동 생성
public class ProfileResponseDto {

    // 프로필 대상 사용자 ID
    private Long userId;

    // 사용자 이메일
    private String email;

    // 사용자 이름
    private String name;

    // 경력 또는 직무 정보
    private String career;

    // 나이 정보
    private Integer age;
}
