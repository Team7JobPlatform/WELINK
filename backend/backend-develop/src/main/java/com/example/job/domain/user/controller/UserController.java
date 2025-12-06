package com.example.job.domain.user.controller;

import com.example.job.domain.user.dto.*;
import com.example.job.domain.user.entity.User;
import com.example.job.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController                         // 사용자 관련 REST API 컨트롤러
@RequestMapping("/api/users")           // 기본 URL: /api/users
@RequiredArgsConstructor                // 생성자 주입 자동 생성
public class UserController {

    private final UserService userService; // 사용자 비즈니스 로직 처리 서비스

    // 회원가입 API
    // POST /api/users/signup
    @PostMapping("/signup")
    public ResponseEntity<Long> signup(@RequestBody SignupRequestDto dto) {
        Long userId = userService.signup(dto); // 회원 생성 후 ID 반환
        return ResponseEntity.ok(userId);
    }

    // 로그인 API (JWT 발급)
    // POST /api/users/login
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto dto) {
        LoginResponseDto response = userService.login(dto); // 이메일/비밀번호 검증 + 토큰 발급
        return ResponseEntity.ok(response);
    }

    // 프로필 조회 API
    // GET /api/users/{userId}/profile
    @GetMapping("/{userId}/profile")
    public ResponseEntity<ProfileResponseDto> getProfile(@PathVariable Long userId) {
        ProfileResponseDto response = userService.getProfile(userId); // 특정 유저 프로필 조회
        return ResponseEntity.ok(response);
    }

    // 프로필 수정 API
    // PUT /api/users/{userId}/profile
    @PutMapping("/{userId}/profile")
    public ResponseEntity<ProfileResponseDto> updateProfile(
            @PathVariable Long userId,
            @RequestBody ProfileUpdateRequestDto dto
    ) {
        ProfileResponseDto response = userService.updateProfile(userId, dto); // 프로필 정보 수정
        return ResponseEntity.ok(response);
    }

    // 내 정보 조회 API (JWT 기반)
    // GET /api/users/me
    @GetMapping("/me")
    public ResponseEntity<User> me(org.springframework.security.core.Authentication authentication) {
        // JwtAuthenticationFilter 에서 principal 로 넣어 둔 userId 문자열 꺼내기
        String userIdStr = (String) authentication.getPrincipal();
        Long userId = Long.parseLong(userIdStr);

        // 현재 로그인한 사용자의 엔티티 조회
        User user = userService.getMyPage(userId);
        return ResponseEntity.ok(user);
    }
}
