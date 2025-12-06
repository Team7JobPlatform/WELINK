/*
package com.example.job.domain.company.service;

import com.example.job.domain.company.entity.WelfareItem;
import com.example.job.domain.company.repository.WelfareItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WelfareItemService {

    private final WelfareItemRepository welfareItemRepository;

    // 전체 복지 항목 조회
    public List<WelfareItem> getAllWelfareItems() {
        return welfareItemRepository.findAll();
    }

    // 카테고리별 복지 항목 조회
    public List<WelfareItem> getWelfareItemsByCategory(String category) {
        return welfareItemRepository.findByCategory(category);
    }

    // 복지 항목 상세 조회
    public WelfareItem getWelfareItemById(Long id) {
        return welfareItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("복지 항목을 찾을 수 없습니다."));
    }
}
*/
/*
package com.example.job.domain.company.service;

import com.example.job.domain.company.entity.Company;
import com.example.job.domain.company.repository.CompanyRepository; // ★ CompanyRepository 사용
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WelfareItemService {

    private final CompanyRepository companyRepository; // ★ CompanyRepository로 변경

    public WelfareItemService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    // 복지 항목이 연결된 모든 회사 리스트를 가져옵니다. (기존 메서드 재사용)
    public List<Company> getAllWelfareItems() {
        // 새로 추가한 조인 쿼리 메소드 호출
        return companyRepository.findAllWithWelfareDetails();
    }
}*/
package com.example.job.domain.company.service;

import com.example.job.domain.company.entity.Company; // Company Entity 사용
import com.example.job.domain.company.repository.CompanyRepository; // Company Repository 사용
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WelfareItemService {

    private final CompanyRepository companyRepository;

    public WelfareItemService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    // 모든 회사와 복지 정보를 조인하여 가져옵니다.
    public List<Company> getAllWelfareItems() {
        return companyRepository.findAllWithWelfareDetails();
    }
}