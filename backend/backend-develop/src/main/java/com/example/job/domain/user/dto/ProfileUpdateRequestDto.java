package com.example.job.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter                          // 각 필드에 대한 getter 생성
@NoArgsConstructor               // 파라미터 없는 기본 생성자 (JSON 역직렬화용)
@AllArgsConstructor              // 모든 필드를 받는 생성자 자동 생성
public class ProfileUpdateRequestDto {

    // 수정할 이름
    private String name;

    // 수정할 경력/직무 정보
    private String career;

    // 수정할 나이
    private Integer age;
}
