/*
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
*//*

package com.example.job.domain.company.repository;

import com.example.job.domain.company.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; // Query import 추가
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    // ... (기존 findByLocation 등 메소드는 유지) ...

    */
/**
     * 모든 회사를 조회하며, 연결된 복지 정보(CompanyWelfare)와 그 복지 항목(WelfareItem)까지 JOIN하여 가져옵니다.
     * 이 메소드를 호출하면 모든 복지 관련 데이터를 한 번에 가져올 수 있습니다.
     *//*

    @Query("SELECT c FROM Company c " +
            "LEFT JOIN FETCH c.companyWelfares cw " +
            "LEFT JOIN FETCH cw.welfareItem wi")
    List<Company> findAllWithWelfareDetails();

    List<Company> findByLocation(String location);
}*/
package com.example.job.domain.company.repository;

import com.example.job.domain.company.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    /**
     * 모든 회사를 조회하며, 복지 연결 정보와 그 복지 항목까지 JOIN FETCH하여 가져옵니다.
     * (이 쿼리가 데이터를 가져오는 핵심입니다.)
     */
    @Query("SELECT c FROM Company c " +
            "LEFT JOIN FETCH c.companyWelfares cw " +
            "LEFT JOIN FETCH cw.welfareItem wi")
    List<Company> findAllWithWelfareDetails();
    List<Company> findByLocation(String location);
}