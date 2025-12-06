package com.example.job.domain.company.service;

import com.example.job.domain.company.entity.Company;
import com.example.job.domain.company.entity.CompanyWelfare;
import com.example.job.domain.company.entity.WelfareItem;
import com.example.job.domain.company.repository.CompanyRepository;
import com.example.job.domain.company.repository.CompanyWelfareRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyWelfareRepository companyWelfareRepository;

    // 전체 회사 목록 조회
    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    // 회사 상세 조회
    public Company getCompanyById(Long id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("회사를 찾을 수 없습니다."));
    }

    // 회사별 복지 항목 조회
    public List<WelfareItem> getWelfareItemsByCompanyId(Long companyId) {
        List<CompanyWelfare> companyWelfares = companyWelfareRepository.findByCompanyId(companyId);
        return companyWelfares.stream()
                .map(CompanyWelfare::getWelfareItem)
                .collect(Collectors.toList());
    }

    // 지역별 회사 검색
    public List<Company> getCompaniesByLocation(String location) {
        return companyRepository.findByLocation(location);
    }
}
