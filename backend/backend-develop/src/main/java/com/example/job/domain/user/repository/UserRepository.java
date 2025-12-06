package com.example.job.domain.user.repository;

import com.example.job.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// User 엔티티에 대한 DB 접근을 담당하는 JPA 리포지토리
public interface UserRepository extends JpaRepository<User, Long> {

    // 이메일로 사용자 한 명 조회 (없으면 Optional.empty 반환)
    Optional<User> findByEmail(String email);

    // 해당 이메일을 가진 사용자가 이미 존재하는지 여부 확인
    boolean existsByEmail(String email);
}
