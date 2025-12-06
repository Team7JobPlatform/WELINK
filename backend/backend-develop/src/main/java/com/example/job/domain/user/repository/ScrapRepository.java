package com.example.job.domain.user.repository;

import com.example.job.domain.user.entity.Scrap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Scrap 엔티티에 대한 DB 접근을 담당하는 JPA 리포지토리
public interface ScrapRepository extends JpaRepository<Scrap, Long> {

    // 특정 사용자(userId)가 같은 대상(targetId, targetType)을 이미 스크랩했는지 여부 확인
    boolean existsByUserIdAndTargetIdAndTargetType(Long userId, Long targetId, String targetType);

    // userId 로 해당 사용자의 모든 스크랩 목록 조회
    List<Scrap> findAllByUserId(Long userId);

    // 위와 동일하게 userId 기준으로 스크랩 목록 조회 (용도에 따라 둘 중 하나만 사용해도 됨)
    List<Scrap> findByUserId(Long userId);
}
