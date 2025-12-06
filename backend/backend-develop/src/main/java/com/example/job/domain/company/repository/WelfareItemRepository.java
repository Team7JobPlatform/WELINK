package com.example.job.domain.company.repository;

import com.example.job.domain.company.entity.WelfareItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WelfareItemRepository extends JpaRepository<WelfareItem, Long> {
    List<WelfareItem> findByCategory(String category);
}
