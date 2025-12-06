package com.example.job.domain.user.controller;

import com.example.job.config.JwtTokenProvider;
import com.example.job.domain.user.entity.Scrap;
import com.example.job.domain.user.service.ScrapService;
import com.example.job.domain.user.dto.MyPageResponseDto;
import com.example.job.domain.user.entity.User;
import com.example.job.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController                       // REST API 컨트롤러임을 표시
@RequiredArgsConstructor              // 생성자 주입을 자동으로 생성
@RequestMapping("/api")               // 이 컨트롤러의 기본 URL prefix: /api
public class MyPageController {

    // 회원 정보 조회를 위한 서비스
    private final UserService userService;
    // 스크랩 목록 조회를 위한 서비스
    private final ScrapService scrapService;
    // JWT 에서 userId 를 꺼낼 때 사용하는 유틸
    private final JwtTokenProvider jwtTokenProvider;

    // 마이페이지 API: 현재 로그인한 사용자 정보 + 스크랩 목록 조회
    // GET /api/me
    @GetMapping("/me")
    public MyPageResponseDto me(@RequestHeader("Authorization") String authHeader) {
        // "Bearer {토큰}" 형식에서 실제 토큰 문자열만 분리
        String token = authHeader.replace("Bearer ", "");
        // 토큰에서 userId(subject) 추출
        Long userId = jwtTokenProvider.getUserIdFromToken(token);

        // userId 로 회원 정보 조회
        User user = userService.getMyPage(userId);
        // userId 로 해당 사용자의 스크랩 목록 조회
        List<Scrap> scraps = scrapService.getMyScraps(userId);

        // 회원 정보 + 스크랩 목록을 하나의 DTO 로 묶어서 응답
        return MyPageResponseDto.of(user, scraps);
    }
}
