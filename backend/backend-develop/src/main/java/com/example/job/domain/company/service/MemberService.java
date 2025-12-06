package com.example.job.domain.company.service;

import com.example.job.domain.company.entity.Member;
import com.example.job.domain.company.entity.MemberPreference;
import com.example.job.domain.company.entity.WelfareItem;
import com.example.job.domain.company.repository.MemberRepository;
import com.example.job.domain.company.repository.MemberPreferenceRepository;
import com.example.job.domain.company.repository.WelfareItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberPreferenceRepository memberPreferenceRepository;
    private final WelfareItemRepository welfareItemRepository;

    public MemberService(MemberRepository memberRepository,
                         MemberPreferenceRepository memberPreferenceRepository,
                         WelfareItemRepository welfareItemRepository) {
        this.memberRepository = memberRepository;
        this.memberPreferenceRepository = memberPreferenceRepository;
        this.welfareItemRepository = welfareItemRepository;
    }

    // 회원 등록
    @Transactional
    public Member createMember(String username, String email) {
        Member member = new Member();
        member.setUsername(username);
        member.setEmail(email);
        return memberRepository.save(member);
    }

    // 선호도 추가
    @Transactional
    public MemberPreference addPreference(Long memberId, Long welfareItemId, Integer priority) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));

        WelfareItem welfareItem = welfareItemRepository.findById(welfareItemId)
                .orElseThrow(() -> new RuntimeException("복지 항목을 찾을 수 없습니다."));

        MemberPreference preference = new MemberPreference();
        preference.setMember(member);
        preference.setWelfareItem(welfareItem);
        preference.setPriority(priority);

        return memberPreferenceRepository.save(preference);
    }

    // 회원의 선호도 조회
    public List<MemberPreference> getMemberPreferences(Long memberId) {
        return memberPreferenceRepository.findByMemberId(memberId);
    }

    // 모든 회원 조회
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }
}
