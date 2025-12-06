package com.example.job.domain.company.repository;

import com.example.job.domain.company.entity.CompanyWelfare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyWelfareRepository extends JpaRepository<CompanyWelfare, Long> {
    List<CompanyWelfare> findByCompanyId(Long companyId);
    List<CompanyWelfare> findByWelfareItemId(Long welfareItemId);
}
