package com.example.job.domain.company.repository;

import com.example.job.domain.company.entity.MemberPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberPreferenceRepository extends JpaRepository<MemberPreference, Long> {
    List<MemberPreference> findByMemberId(Long memberId);
}
