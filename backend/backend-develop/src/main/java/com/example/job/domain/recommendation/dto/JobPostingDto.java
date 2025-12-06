package com.example.job.domain.recommendation.dto;

import lombok.Data;

@Data
public class JobPostingDto {
    private String title;       // 채용 제목
    private String deadline;    // 마감일
    private String location;    // 근무지
    private String url;         // 공고 상세 링크
}