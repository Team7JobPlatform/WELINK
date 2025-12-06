package com.example.job.domain.job.controller;

import com.example.job.domain.job.service.JobApiIntegrationService;
import com.example.job.domain.job.entity.JobPosting;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobApiController {

    private final JobApiIntegrationService jobApiIntegrationService;

    public JobApiController(JobApiIntegrationService jobApiIntegrationService) {
        this.jobApiIntegrationService = jobApiIntegrationService;
    }

    /**
     * (테스트용) 외부 API에서 데이터를 가져와 DB에 저장합니다.
     * 엔드포인트: GET /api/jobs/fetch
     */
    @GetMapping("/fetch")
    public String fetchExternalJobs() {
        return jobApiIntegrationService.fetchAndProcessJobs();
    }

    /**
     * (프론트엔드용) DB에 저장된 모든 채용 공고 목록을 조회합니다.
     * 엔드포인트: GET /api/jobs/all
     */
    @GetMapping("/all")
    public ResponseEntity<List<JobPosting>> getAllJobs() {
        List<JobPosting> postings = jobApiIntegrationService.getAllJobPostings();
        return ResponseEntity.ok(postings);
    }
}