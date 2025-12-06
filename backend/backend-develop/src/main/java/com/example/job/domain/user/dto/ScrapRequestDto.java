package com.example.job.domain.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter                          // 각 필드에 대한 getter 생성
@NoArgsConstructor               // 기본 생성자 (JSON 역직렬화용)
public class ScrapRequestDto {

    // 스크랩하려는 대상의 ID (예: 공고 ID, 회사 ID 등)
    private Long targetId;

    // 스크랩 대상 종류 (예: "JOB", "COMPANY")
    private String targetType;
}
