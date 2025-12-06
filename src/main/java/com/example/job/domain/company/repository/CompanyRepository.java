package com.example.job.domain.company.repository;

import com.example.job.domain.company.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    List<Company> findByLocation(String location);

    @Query("SELECT DISTINCT c FROM Company c LEFT JOIN FETCH c.companyWelfares")
    List<Company> findAllWithWelfares();

}
