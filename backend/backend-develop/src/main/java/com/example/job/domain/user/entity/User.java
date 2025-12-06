// User.java
package com.example.job.domain.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Entity
@Table(name = "users")                        // DB 테이블 이름: users
@Getter
@Setter
@NoArgsConstructor                            // 기본 생성자
@AllArgsConstructor                           // 모든 필드를 받는 생성자
@Builder                                      // 빌더 패턴 사용
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가 PK
    private Long id;

    @Column(nullable = false, unique = true)  // 필수 + 유니크 제약
    private String email;

    @Column(nullable = false)                 // 암호화된 비밀번호 저장
    private String password;

    @Column(nullable = false)                 // 사용자 이름
    private String name;

    @Column(nullable = false)                 // 권한 정보 (예: ROLE_USER)
    private String role;

    @Column                                    // 경력/직무 정보 (옵션)
    private String career;

    @Column                                    // 나이 정보 (옵션)
    private Integer age;
}
