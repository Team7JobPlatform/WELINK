package com.example.job.domain.job.service;

import com.example.job.domain.job.repository.JobPostingRepository;
import com.example.job.domain.job.entity.JobPosting;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.Data;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobApiIntegrationService {

    // --- 1. 외부 API 응답 구조를 위한 DTO 정의 (파싱 에러 해결 완료) ---
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ApiJobItem {

        // JSON 필드명과 Entity 필드명 매핑 (recrutPbancTtl -> title)
        @JsonProperty("recrutPbancTtl")
        private String title;

        @JsonProperty("workRgnNmLst")
        private String location;

        @JsonProperty("pbancEndYmd")
        private String deadline;

        @JsonProperty("srcUrl")
        private String url;

        // DTO를 Entity로 변환하는 메서드
        public JobPosting toEntity() {
            JobPosting post = new JobPosting();
            post.setTitle(this.title);
            post.setLocation(this.location);
            post.setDeadline(this.deadline);
            post.setOriginalUrl(this.url); // originalUrl로 저장
            return post;
        }
    }

    // 최상위 Wrapper (totalCount 무시 설정으로 에러 해결)
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ApiResponseWrapper {
        private Integer resultCode;
        private String resultMsg;
        private List<ApiJobItem> result;
    }
    // -------------------------------------------------------------------


    @Value("${job.api.base-url}")
    private String apiBaseUrl;

    @Value("${job.api.service-key}")
    private String apiServiceKey;

    @Value("${job.api.default-params}")
    private String apiDefaultParams;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final JobPostingRepository repository;

    public JobApiIntegrationService(RestTemplate restTemplate, ObjectMapper objectMapper, JobPostingRepository repository) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.repository = repository;
    }

    /**
     * 외부 API를 호출하여 데이터를 가져와 DB에 저장하는 로직
     */
    public String fetchAndProcessJobs() {
        try {
            String finalUrl = apiBaseUrl
                    + apiDefaultParams
                    + "&serviceKey=" + apiServiceKey;

            String rawJson = restTemplate.getForObject(finalUrl, String.class);

            // 파싱: JSON Wrapper에 맞춰 DTO 사용
            ApiResponseWrapper wrapper = objectMapper.readValue(rawJson, ApiResponseWrapper.class);

            if (wrapper.getResultCode() != 200 || wrapper.getResult() == null) {
                return "API 응답 오류: " + wrapper.getResultMsg();
            }

            // Entity로 변환 및 List 추출
            List<JobPosting> jobPostings = wrapper.getResult().stream()
                    .map(ApiJobItem::toEntity)
                    .collect(Collectors.toList());

            // DB에 저장
            repository.saveAll(jobPostings);

            return "외부 API에서 채용 정보 " + jobPostings.size() + "개를 성공적으로 가져와 DB에 저장했습니다.";

        } catch (Exception e) {
            System.err.println("API Fetch 및 파싱 실패: " + e.getMessage());
            e.printStackTrace();
            return "API Fetch 및 파싱 실패: " + e.getMessage();
        }
    }

    /**
     * DB에 저장된 모든 채용 공고를 조회합니다. (프론트엔드 제공용)
     */
    public List<JobPosting> getAllJobPostings() {
        return repository.findAll();
    }
}