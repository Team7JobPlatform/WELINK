package com.example.job.domain.company.controller;

import com.example.job.domain.company.entity.Member;
import com.example.job.domain.company.entity.MemberPreference;
import com.example.job.domain.company.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // 회원 등록
    @PostMapping
    public ResponseEntity<Member> createMember(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String email = request.get("email");
        Member member = memberService.createMember(username, email);
        return ResponseEntity.ok(member);
    }

    // 선호도 추가
    @PostMapping("/{memberId}/preferences")
    public ResponseEntity<MemberPreference> addPreference(
            @PathVariable Long memberId,
            @RequestBody Map<String, Object> request) {
        Long welfareItemId = Long.valueOf(request.get("welfareItemId").toString());
        Integer priority = Integer.valueOf(request.get("priority").toString());

        MemberPreference preference = memberService.addPreference(memberId, welfareItemId, priority);
        return ResponseEntity.ok(preference);
    }

    // 회원의 선호도 조회
    @GetMapping("/{memberId}/preferences")
    public ResponseEntity<List<MemberPreference>> getMemberPreferences(@PathVariable Long memberId) {
        List<MemberPreference> preferences = memberService.getMemberPreferences(memberId);
        return ResponseEntity.ok(preferences);
    }

    // 모든 회원 조회
    @GetMapping
    public ResponseEntity<List<Member>> getAllMembers() {
        List<Member> members = memberService.getAllMembers();
        return ResponseEntity.ok(members);
    }
}
