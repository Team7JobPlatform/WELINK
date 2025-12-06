package com.example.job.domain.company.service;

import com.example.job.domain.company.dto.RecommendationDto;
import com.example.job.domain.company.entity.Company;
import com.example.job.domain.company.entity.CompanyWelfare;
import com.example.job.domain.company.entity.MemberPreference;
import com.example.job.domain.company.repository.CompanyRepository;
import com.example.job.domain.company.repository.CompanyWelfareRepository;
import com.example.job.domain.company.repository.MemberPreferenceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class RecommendationService {

    private final MemberPreferenceRepository memberPreferenceRepository;
    private final CompanyRepository companyRepository;
    private final CompanyWelfareRepository companyWelfareRepository;

    public RecommendationService(MemberPreferenceRepository memberPreferenceRepository,
                                 CompanyRepository companyRepository,
                                 CompanyWelfareRepository companyWelfareRepository) {
        this.memberPreferenceRepository = memberPreferenceRepository;
        this.companyRepository = companyRepository;
        this.companyWelfareRepository = companyWelfareRepository;
    }

    // 회원 ID로 추천 회사 목록 (점수순)
    public List<RecommendationDto> recommendCompanies(Long memberId) {
        // 1. 회원의 복지 선호도 가져오기
        List<MemberPreference> preferences = memberPreferenceRepository.findByMemberId(memberId);

        if (preferences.isEmpty()) {
            return Collections.emptyList();
        }

        // 2. 선호 복지 ID와 우선순위 맵
        Map<Long, Integer> preferenceMap = preferences.stream()
                .collect(Collectors.toMap(
                        p -> p.getWelfareItem().getId(),
                        MemberPreference::getPriority
                ));

        // 3. 모든 회사 가져오기
        List<Company> companies = companyRepository.findAll();

        // 4. 각 회사별 점수 계산 후 DTO로 변환
        List<RecommendationDto> results = new ArrayList<>();

        for (Company company : companies) {
            int score = calculateScore(company.getId(), preferenceMap);
            int matchedCount = countMatchedWelfares(company.getId(), preferenceMap.keySet());

            RecommendationDto dto = new RecommendationDto(
                    company.getId(),
                    company.getName(),
                    company.getLocation(),
                    score,
                    matchedCount
            );

            results.add(dto);
        }

        // 5. 점수 높은 순으로 정렬
        results.sort((a, b) -> Integer.compare(b.getScore(), a.getScore()));

        return results;
    }

    // 회사의 복지 점수 계산
    private int calculateScore(Long companyId, Map<Long, Integer> preferenceMap) {
        List<CompanyWelfare> companyWelfares = companyWelfareRepository.findByCompanyId(companyId);

        int totalScore = 0;
        for (CompanyWelfare cw : companyWelfares) {
            Long welfareId = cw.getWelfareItem().getId();
            if (preferenceMap.containsKey(welfareId)) {
                totalScore += preferenceMap.get(welfareId);
            }
        }

        return totalScore;
    }

    // 매칭된 복지 개수
    private int countMatchedWelfares(Long companyId, Set<Long> preferredWelfareIds) {
        List<CompanyWelfare> companyWelfares = companyWelfareRepository.findByCompanyId(companyId);

        return (int) companyWelfares.stream()
                .map(cw -> cw.getWelfareItem().getId())
                .filter(preferredWelfareIds::contains)
                .count();
    }

    public List<RecommendationDto> getRecommendations(Long memberId) {
        return List.of();
    }
}

