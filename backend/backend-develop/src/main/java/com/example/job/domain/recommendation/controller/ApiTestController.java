package com.example.job.domain.recommendation.controller;

import com.example.job.domain.recommendation.dto.JobPostingDto;
import com.example.job.domain.recommendation.service.JobPostingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ApiTestController {

    private final JobPostingService jobPostingService;

    // 테스트 주소: http://localhost:8080/api/test/open-api?name=기업명
    @GetMapping("/api/test/open-api")
    public JobPostingDto testOpenApi(@RequestParam String name) {

        System.out.println("====== [TEST] 공공 API 호출 시작: " + name + " ======");

        // 내 서비스만 단독으로 호출!
        JobPostingDto result = jobPostingService.getJobPosting(name);

        return result; // 결과(DTO)를 브라우저에 바로 보여줌
    }
}