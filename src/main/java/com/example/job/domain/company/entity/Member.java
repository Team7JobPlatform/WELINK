package com.example.job.domain.company.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username; // 아이디
    private String email;    // 이메일

    // ▼▼▼ 이 3개가 추가되어야 합니다! ▼▼▼
    private String password; // 비밀번호
    private String name;     // 이름
    private String phone;    // 전화번호
    // ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<MemberPreference> preferences = new ArrayList<>();

    // --- Getter & Setter (필수!) ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    // ▼ 추가된 필드들에 대한 Getter/Setter도 꼭 있어야 합니다!
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public List<MemberPreference> getPreferences() { return preferences; }
    public void setPreferences(List<MemberPreference> preferences) { this.preferences = preferences; }
}