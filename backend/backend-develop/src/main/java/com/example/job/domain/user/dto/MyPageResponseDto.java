package com.example.job.domain.user.dto;

import com.example.job.domain.user.entity.User;
import com.example.job.domain.user.entity.Scrap;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter                                   // 모든 필드에 대한 getter 생성
@AllArgsConstructor                      // 모든 필드를 받는 생성자 자동 생성
public class MyPageResponseDto {

    // 현재 로그인한 사용자 정보
    private Long userId;
    private String name;
    private String email;
    private String career;
    private Integer age;

    // 사용자가 찜한(Scrap) 목록
    private List<Scrap> scraps; // 필요하면 ScrapDto 리스트로 변경 가능

    // User 엔티티와 Scrap 목록을 한 번에 DTO 로 변환하는 편의 메서드
    public static MyPageResponseDto of(User user, List<Scrap> scraps) {
        return new MyPageResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCareer(),
                user.getAge(),
                scraps
        );
    }
}
