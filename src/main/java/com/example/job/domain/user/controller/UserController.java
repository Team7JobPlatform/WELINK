package com.example.job.domain.user.controller;

import com.example.job.domain.user.entity.User;
import com.example.job.domain.user.service.UserService;
import com.example.job.config.JwtTokenProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import javax.servlet.http.HttpServletRequest; // ★ resolveToken을 위해 필수
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;
import io.jsonwebtoken.JwtException; // JWT 예외 처리를 위해 필수

@RestController
@RequestMapping("/api/users")
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    public UserController(UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // [POST] /api/users/signup (회원가입)
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody User user) {
        try {
            User savedUser = userService.signup(user);
            return ResponseEntity.ok("가입 성공! (이름: " + savedUser.getName() + ")");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("실패: " + e.getMessage());
        }
    }

    // [POST] /api/users/login (로그인)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {
        try {
            String email = loginData.get("email");
            String password = loginData.get("password");

            User user = userService.login(email, password);

            // JWT 토큰 생성
            String token = jwtTokenProvider.createToken(user.getId(), user.getEmail(), user.getRole());

            // JSON 형태로 토큰과 사용자 정보를 반환
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("userName", user.getName());
            response.put("userId", user.getId());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("로그인 실패: " + e.getMessage());
        }
    }

    // ★★★ [마이페이지 핵심] GET /api/users/me 엔드포인트 ★★★
    @GetMapping("/me")
    public ResponseEntity<?> getMyInfo(HttpServletRequest request) {
        try {
            // 1. 요청 헤더에서 토큰 추출
            String token = jwtTokenProvider.resolveToken(request);

            if (token == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증 토큰이 누락되었습니다.");
            }

            // 2. 토큰에서 사용자 이메일 추출
            String email = jwtTokenProvider.getUserEmail(token);

            // 3. 이메일로 DB에서 User 엔티티 조회 (UserService의 findByEmail 사용)
            Optional<User> userOptional = userService.findByEmail(email);

            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자 정보를 찾을 수 없습니다.");
            }

            User user = userOptional.get();

            // 4. 클라이언트에게 전달할 JSON 데이터 구성
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("id", user.getId());
            userInfo.put("email", user.getEmail());
            userInfo.put("name", user.getName());
            userInfo.put("role", user.getRole());

            return ResponseEntity.ok(userInfo);

        } catch (JwtException e) {
            // 토큰 만료 또는 변조 에러
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰이 유효하지 않거나 만료되었습니다.");
        } catch (Exception e) {
            // 기타 DB 조회 에러 등
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("사용자 정보 로딩 중 오류 발생: " + e.getMessage());
        }
    }
}