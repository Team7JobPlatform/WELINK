package com.example.job.domain.user.dto;

import com.example.job.domain.user.entity.User;
import com.example.job.domain.user.entity.Scrap;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MyPageResponseDto {

    // 현재 로그인한 사용자 정보 (User 엔티티의 필드와 일치)
    private Long id;

    private String username; // 아이디
    private String email;    // 이메일

    // ▼▼▼ 이 3개가 추가되어야 합니다! ▼▼▼
    private String password; // 비밀번호
    private String name;     // 이름
    private String phone;    // User 엔티티의 age 필드 (Integer)

    // 사용자가 찜한(Scrap) 목록
    private List<Scrap> scraps; // (List<Scrap>으로 유지)

    /**
     * User 엔티티와 Scrap 목록을 받아 MyPageResponseDto로 변환하는 정적 팩토리 메서드
     */
    public static MyPageResponseDto of(User user, List<Scrap> scraps) {
        // User 엔티티의 Getter를 사용하여 DTO 필드에 매핑합니다.
        return new MyPageResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getUsername(),
                user.getPassword(),
                user.getPhone(),
                scraps
        );
    }
}