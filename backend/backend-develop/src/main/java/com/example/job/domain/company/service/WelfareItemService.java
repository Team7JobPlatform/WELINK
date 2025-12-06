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
