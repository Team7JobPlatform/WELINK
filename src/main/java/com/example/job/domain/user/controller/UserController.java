package com.example.job.domain.user.controller;

import com.example.job.domain.user.entity.User;
import com.example.job.domain.user.service.UserService;
import com.example.job.config.JwtTokenProvider;
import com.example.job.domain.company.entity.Company;
import com.example.job.domain.company.repository.CompanyRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import jakarta.servlet.http.HttpServletRequest;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final CompanyRepository companyRepository;

    public UserController(UserService userService, JwtTokenProvider jwtTokenProvider, CompanyRepository companyRepository) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.companyRepository = companyRepository;
    }

    // [1] 회원가입
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody User user) {
        try { userService.signup(user); return ResponseEntity.ok("가입 성공!"); }
        catch (Exception e) { return ResponseEntity.badRequest().body("실패: " + e.getMessage()); }
    }

    // [2] 로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {
        try {
            User user = userService.login(loginData.get("email"), loginData.get("password"));
            String token = jwtTokenProvider.createToken(user.getId(), user.getEmail(), user.getRole());
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("userName", user.getName());
            return ResponseEntity.ok(response);
        } catch (Exception e) { return ResponseEntity.badRequest().body("로그인 실패"); }
    }

    // [3] 내 정보
    @GetMapping("/me")
    public ResponseEntity<?> getMyInfo(HttpServletRequest request) {
        try {
            String token = jwtTokenProvider.resolveToken(request);
            if (token == null) return ResponseEntity.status(401).build();
            User user = userService.findByEmail(jwtTokenProvider.getUserEmail(token)).orElseThrow();
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("email", user.getEmail());
            userInfo.put("name", user.getName());
            return ResponseEntity.ok(userInfo);
        } catch (Exception e) { return ResponseEntity.status(401).build(); }
    }

    // [4] 대시보드 (초기 0으로 설정)
    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboardStats(HttpServletRequest request) {
        try {
            String token = jwtTokenProvider.resolveToken(request);
            if (token == null) return ResponseEntity.status(401).build();

            // 아직 추천받기 전이므로 0으로 표시
            Map<String, Integer> stats = new HashMap<>();
            stats.put("resumeCount", 3);
            stats.put("interestCount", 5);
            stats.put("recommendCount", 0);
            stats.put("welfareScore", 0);
            stats.put("qnaCount", 0);
            return ResponseEntity.ok(stats);
        } catch (Exception e) { return ResponseEntity.badRequest().body("Error"); }
    }

    // ★★★ [5] 추천 로직 (매핑 강화 + 랜덤 섞기) ★★★
    @PostMapping("/preferences")
    public ResponseEntity<?> savePreferences(HttpServletRequest request, @RequestBody Map<String, Integer> scores) {
        try {
            String token = jwtTokenProvider.resolveToken(request);
            if (token == null) return ResponseEntity.status(401).body("로그인 필요");

            // 1. 데이터 확인 (DB가 비었는지 체크)
            long totalCompanies = companyRepository.count();
            if (totalCompanies == 0) {
                System.out.println(">>> [경고] DB에 기업 데이터가 없습니다! data.sql을 확인하세요.");
                // 임시 데이터라도 반환
                return ResponseEntity.ok(Arrays.asList(Map.of("name", "데이터 없음", "desc", "서버 DB를 확인해주세요.", "tags", "#오류")));
            }

            System.out.println(">>> 사용자 선택: " + scores);

            Set<Company> recommendedSet = new HashSet<>();

            // 2. 매핑 (프론트엔드 버튼 이름 -> DB 복지 이름)
            Map<String, String> keywordMap = new HashMap<>();
            // HTML의 data-name과 정확히 일치해야 함
            keywordMap.put("재택근무", "재택근무");
            keywordMap.put("유연근무제", "유연근무제");
            keywordMap.put("주 4.5일", "자율출퇴근");
            keywordMap.put("자유로운 연차", "유연근무제");
            keywordMap.put("야근없음", "비포괄임금제");

            keywordMap.put("성과급", "성과급");
            keywordMap.put("보너스", "성과급");
            keywordMap.put("스톡옵션", "스톡옵션");
            keywordMap.put("식대 지원", "사내식당");
            keywordMap.put("복지 포인트", "복지포인트");
            keywordMap.put("통근버스", "통근버스");

            // 3. DB 검색
            for (String key : scores.keySet()) {
                int score = scores.get(key);
                if (score >= 1) {
                    String dbKeyword = keywordMap.get(key);
                    if (dbKeyword != null) {
                        String searchPattern = "%" + dbKeyword + "%";
                        List<Company> found = companyRepository.findByWelfareNameContaining(searchPattern);
                        recommendedSet.addAll(found);
                    }
                }
            }

            System.out.println(">>> 검색된 총 기업 수: " + recommendedSet.size() + "개");

            // 4. 결과 리스트 변환
            List<Map<String, String>> resultList = recommendedSet.stream()
                    .map(c -> {
                        Map<String, String> map = new HashMap<>();
                        map.put("name", c.getName());
                        String desc = (c.getDescription() != null) ? c.getDescription() : "복지 우수 기업";
                        map.put("desc", desc);
                        map.put("tags", "#" + c.getIndustry() + " #" + c.getLocation());
                        return map;
                    }).collect(Collectors.toList());

            // 5. ★★★ [중요] 결과를 무작위로 섞음 ★★★
            Collections.shuffle(resultList);

            // 6. 결과가 없으면 랜덤으로 아무거나 추천
            if (resultList.isEmpty()) {
                List<Company> allCompanies = companyRepository.findAll();
                Collections.shuffle(allCompanies); // 전체 기업도 섞어서
                resultList = allCompanies.stream().limit(4).map(c -> {
                    Map<String, String> map = new HashMap<>();
                    map.put("name", c.getName());
                    map.put("desc", "기본 추천 기업");
                    map.put("tags", "#" + c.getIndustry());
                    return map;
                }).collect(Collectors.toList());
            }

            // 7. 최대 6개까지만 자르기
            if (resultList.size() > 6) {
                resultList = resultList.subList(0, 6);
            }

            return ResponseEntity.ok(resultList);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("오류: " + e.getMessage());
        }
    }
}