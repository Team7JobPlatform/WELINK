package com.example.job.domain.recommendation.controller;

// [Import] 다른 팀원의 코드 (company 패키지에 있다고 가정)
import com.example.job.domain.company.dto.RecommendationDto;
import com.example.job.domain.company.service.RecommendationService;

// [Import] 내 코드 (recommendation 패키지)
import com.example.job.domain.recommendation.dto.FinalRecommendationDto;
import com.example.job.domain.recommendation.dto.JobPostingDto;
import com.example.job.domain.recommendation.service.JobPostingService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController("userRecommendationController")
@RequestMapping("/api/recommendation")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173") // 프론트엔드 연동 허용
public class RecommendationController {

    // 1. 다른 팀원이 만든 AI 매칭 서비스 주입
    private final RecommendationService recommendationService;

    // 2. 내가 만든 공공데이터 연동 서비스 주입
    private final JobPostingService jobPostingService;

    @GetMapping("/{memberId}")
    public List<FinalRecommendationDto> getRecommendations(@PathVariable Long memberId) {

        // Step 1: 다른 사람의 AI 로직을 호출해서 '추천 기업 리스트'를 받아옴
        // (반환 타입: List<RecommendationDto>)
        List<RecommendationDto> aiResults = recommendationService.getRecommendations(memberId);

        List<FinalRecommendationDto> finalResults = new ArrayList<>();

        // Step 2: 받아온 기업 리스트를 하나씩 돌면서 '실시간 공고'를 조회해서 붙임
        for (RecommendationDto aiDto : aiResults) {

            // 회사 이름으로 워크넷(공공API) 조회
            JobPostingDto jobDto = jobPostingService.getJobPosting(aiDto.getCompanyName());

            // 최종 결과 객체(FinalRecommendationDto)에 합치기
            FinalRecommendationDto finalDto = new FinalRecommendationDto();
            finalDto.setCompanyId(aiDto.getCompanyId());
            finalDto.setCompanyName(aiDto.getCompanyName());
            finalDto.setMatchScore(aiDto.getScore()); // AI 점수 복사
            finalDto.setJobPosting(jobDto);           // 실시간 공고 탑재!

            finalResults.add(finalDto);
        }

        return finalResults; // 최종 결과 반환
    }
}
