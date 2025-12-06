// Scrap.java
package com.example.job.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "scraps")                 // DB 테이블 이름: scraps
@Getter
@Setter
@NoArgsConstructor                      // 기본 생성자
@AllArgsConstructor                     // 모든 필드를 받는 생성자
@Builder                                 // 빌더 패턴 사용을 위한 애노테이션
public class Scrap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가 PK
    private Long id;

    // 여러 스크랩이 하나의 유저에 속하는 N:1 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")       // FK 컬럼 이름: user_id
    private User user;

    // 스크랩 대상의 ID (예: 공고 ID, 회사 ID 등)
    private Long targetId;

    // 스크랩 대상의 타입 (예: JOB, COMPANY 등)
    private String targetType;
}
