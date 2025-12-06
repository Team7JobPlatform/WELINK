package com.example.job.domain.user.controller;

import com.example.job.domain.user.dto.MessageResponse;
import com.example.job.domain.user.dto.ScrapRequestDto;
import com.example.job.domain.user.entity.Scrap;
import com.example.job.domain.user.service.ScrapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController                             // 스크랩(찜하기) 관련 REST API 컨트롤러
@RequestMapping("/api/scraps")             // 기본 URL: /api/scraps
@RequiredArgsConstructor                   // 생성자 주입 자동 생성
public class ScrapController {

    private final ScrapService scrapService; // 스크랩 비즈니스 로직을 담당하는 서비스

    // 스크랩 생성 API (찜하기)
    // POST /api/scraps
    @PostMapping
    public ResponseEntity<MessageResponse> createScrap(
            @RequestBody ScrapRequestDto dto,     // 어떤 대상을 스크랩할지 정보
            Authentication authentication         // JWT 필터가 넣어 준 인증 객체
    ) {
        // JwtAuthenticationFilter 에서 설정한 principal(userId 문자열) 꺼내기
        String userIdStr = (String) authentication.getPrincipal();
        Long userId = Long.parseLong(userIdStr);

        // 해당 사용자 기준으로 스크랩 생성
        scrapService.createScrap(userId, dto);

        // 단순 성공 메시지 반환
        return ResponseEntity.ok(new MessageResponse("스크랩 완료"));
    }

    // 내 스크랩 목록 조회 API
    // GET /api/scraps
    @GetMapping
    public ResponseEntity<List<Scrap>> getMyScraps(Authentication authentication) {
        String userIdStr = (String) authentication.getPrincipal();
        Long userId = Long.parseLong(userIdStr);

        // 현재 사용자(userId)의 스크랩 리스트 조회
        List<Scrap> scraps = scrapService.getScraps(userId);
        return ResponseEntity.ok(scraps);
    }

    // 스크랩 삭제 API (찜하기 취소)
    // DELETE /api/scraps/{scrapId}
    @DeleteMapping("/{scrapId}")
    public ResponseEntity<MessageResponse> deleteScrap(
            @PathVariable Long scrapId,          // 삭제할 스크랩 ID
            Authentication authentication
    ) {
        String userIdStr = (String) authentication.getPrincipal();
        Long userId = Long.parseLong(userIdStr);

        // 본인 소유 스크랩인지 확인 후 삭제
        scrapService.deleteScrap(userId, scrapId);

        return ResponseEntity.ok(new MessageResponse("삭제 완료"));
    }
}
