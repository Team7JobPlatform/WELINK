/*
package com.example.job.domain.user.service;

import com.example.job.domain.user.entity.User;
import com.example.job.domain.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 1. 회원가입 (Signup)
    public User signup(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("이미 가입된 이메일입니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        // 기본 권한 설정
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("ROLE_USER");
        }

        return userRepository.save(user);
    }

    // 2. 로그인 (Login)
// UserService.java 파일 내용 중 'login' 메소드만 아래로 교체하세요.

    // 2. 로그인 (Login)
    public User login(String email, String password) {
        // 1. 아이디 존재 여부 확인 (디버그 코드 추가)
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            // DB에 없는 이메일인 경우
            System.out.println("### DEBUG FAIL: Email [" + email + "] NOT found in DB. Throwing rejection.");
            throw new RuntimeException("가입되지 않은 이메일입니다.");
        }

        User user = userOptional.get();

        // 디버그: 어떤 사용자를 찾았는지 출력 (이 로그가 뜨면 문제가 있는 것입니다!)
        System.out.println("### DEBUG PASS: Found User ID " + user.getId() + " for email " + email);

        // 2. 비밀번호 일치 여부 확인
        if (!passwordEncoder.matches(password, user.getPassword())) {
            System.out.println("### DEBUG: Password mismatch for User ID " + user.getId());
            throw new RuntimeException("비밀번호가 틀렸습니다.");
        }

        return user;
    }

    // 3. 마이페이지 정보 조회 (getMyPage)
    // (현재는 User 엔티티만 반환하지만, DTO 로직이 추가되면 확장 가능)
    public User getMyPage(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("해당 ID의 사용자를 찾을 수 없습니다."));
    }

    // (추가된 기능들: ID로 사용자 찾기 등)
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
    }
}*/
package com.example.job.domain.user.service;

import com.example.job.domain.user.entity.User;
import com.example.job.domain.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional; // Optional import 추가
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 1. 회원가입 (Signup)
    public User signup(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("이미 가입된 이메일입니다.");
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("ROLE_USER");
        }

        return userRepository.save(user);
    }

    // 2. 로그인 (Login) - (디버그 코드 제거된 최종 버전)
    public User login(String email, String password) {
        // 1. 아이디 존재 여부 확인
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("가입되지 않은 이메일입니다."));

        // 2. 비밀번호 일치 여부 확인
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("비밀번호가 틀렸습니다.");
        }

        return user;
    }

    // 3. ID로 사용자 찾기 (JWT 필터에서 사용될 수 있음)
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
    }

    // 4. ★★★ [마이페이지 필수] 이메일로 사용자 정보 조회 기능 ★★★
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    // 3. 마이페이지 정보 조회 (getMyPage)
    // (Controller가 호출하는 바로 그 메소드입니다.)
    public User getMyPage(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("해당 ID의 사용자를 찾을 수 없습니다."));
    }
}