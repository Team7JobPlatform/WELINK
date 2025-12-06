package com.example.job.domain.user.service;

import com.example.job.domain.user.dto.ScrapRequestDto;
import com.example.job.domain.user.entity.Scrap;
import com.example.job.domain.user.entity.User;
import com.example.job.domain.user.repository.ScrapRepository;
import com.example.job.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service                                     // 스프링 서비스 빈으로 등록
@RequiredArgsConstructor                    // final 필드를 사용하는 생성자 자동 생성
public class ScrapService {

    private final ScrapRepository scrapRepository; // 스크랩 관련 DB 접근
    private final UserRepository userRepository;   // 사용자 조회용 리포지토리

    // 스크랩 생성(찜하기) 비즈니스 로직
    public Long createScrap(Long userId, ScrapRequestDto dto) {

        // 같은 사용자·같은 대상에 대한 스크랩이 이미 존재하는지 검사
        if (scrapRepository.existsByUserIdAndTargetIdAndTargetType(
                userId, dto.getTargetId(), dto.getTargetType()
        )) {
            throw new IllegalArgumentException("이미 찜한 대상입니다.");
        }

        // 스크랩을 등록할 사용자 조회 (없으면 예외)
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 새 Scrap 엔티티 생성
        Scrap scrap = Scrap.builder()
                .user(user)
                .targetId(dto.getTargetId())
                .targetType(dto.getTargetType())
                .build();

        // 저장 후 생성된 스크랩의 id 반환
        return scrapRepository.save(scrap).getId();
    }

    // 현재 사용자(userId)의 스크랩 목록 조회
    public List<Scrap> getScraps(Long userId) {
        return scrapRepository.findAllByUserId(userId);
    }

    // 스크랩 삭제(찜하기 취소) 비즈니스 로직
    public void deleteScrap(Long userId, Long scrapId) {
        // 삭제하려는 스크랩 조회 (없으면 예외)
        Scrap scrap = scrapRepository.findById(scrapId)
                .orElseThrow(() -> new IllegalArgumentException("스크랩을 찾을 수 없습니다."));

        // 해당 스크랩이 현재 사용자 소유인지 검사
        if (!scrap.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("본인의 스크랩만 삭제할 수 있습니다.");
        }

        // 검증 통과 시 스크랩 삭제
        scrapRepository.delete(scrap);
    }

    // 마이페이지에서 사용할 내 스크랩 목록 조회
    public List<Scrap> getMyScraps(Long userId) {
        return scrapRepository.findByUserId(userId);
    }
}
