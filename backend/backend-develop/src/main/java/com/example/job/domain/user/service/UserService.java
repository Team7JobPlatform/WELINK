package com.example.job.domain.user.service;

import com.example.job.config.JwtTokenProvider;
import com.example.job.domain.user.dto.*;
import com.example.job.domain.user.entity.User;
import com.example.job.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service                                   // 사용자 관련 비즈니스 로직을 담당하는 서비스 빈
@RequiredArgsConstructor                  // userRepository, passwordEncoder, jwtTokenProvider 생성자 주입
public class UserService {

    private final UserRepository userRepository;     // User 엔티티 DB 접근
    private final PasswordEncoder passwordEncoder;   // 비밀번호 암호화/검증
    private final JwtTokenProvider jwtTokenProvider; // JWT 생성 및 파싱 유틸

    // 회원가입 비즈니스 로직
    public Long signup(SignupRequestDto dto) {
        // 이메일 중복 여부 검사
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        // 새 사용자 엔티티 생성 (비밀번호는 암호화해서 저장)
        User user = User.builder()
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .name(dto.getName())
                .role("ROLE_USER")   // 기본 권한
                .build();

        // 저장 후 생성된 사용자 ID 반환
        return userRepository.save(user).getId();
    }

    // 로그인 + JWT 발급 비즈니스 로직
    public LoginResponseDto login(LoginRequestDto dto) {
        // 이메일로 사용자 조회 (없으면 예외)
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다."));

        // 비밀번호 일치 여부 확인
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다.");
        }

        // 검증 성공 시 JWT 액세스 토큰 생성
        String jwt = jwtTokenProvider.createToken(
                user.getId(),
                user.getEmail(),
                user.getRole()
        );

        // 클라이언트에 userId, 이름, 토큰을 함께 반환
        return new LoginResponseDto(
                user.getId(),
                user.getName(),
                jwt
        );
    }

    // 특정 사용자의 프로필 조회
    public ProfileResponseDto getProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));

        return new ProfileResponseDto(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getCareer(),
                user.getAge()
        );
    }

    // 프로필 수정 비즈니스 로직
    public ProfileResponseDto updateProfile(Long userId, ProfileUpdateRequestDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));

        // 전달받은 값으로 필드 수정
        user.setName(dto.getName());
        user.setCareer(dto.getCareer());
        user.setAge(dto.getAge());

        // 수정된 내용 저장
        User saved = userRepository.save(user);

        // 저장 결과를 DTO 로 변환해 반환
        return new ProfileResponseDto(
                saved.getId(),
                saved.getEmail(),
                saved.getName(),
                saved.getCareer(),
                saved.getAge()
        );
    }

    // 마이페이지용: userId 로 내 정보 조회
    public User getMyPage(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalIdentifierException("회원 정보를 찾을 수 없습니다"));
    }
}
