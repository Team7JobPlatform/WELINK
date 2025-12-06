/*
package com.example.job.domain.company.entity;

import jakarta.persistence.*;

@Entity
public class WelfareItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 근무환경 / 보상금융 / 생활편의 / 성장문화 / 건강가족 등
    private String category;

    // 예: "재택근무", "유연근무제"
    private String name;

    // 선택: 설명
    private String description;

    protected WelfareItem() {}

    public WelfareItem(String category, String name, String description) {
        this.category = category;
        this.name = name;
        this.description = description;
    }

    public Long getId() { return id; }
    public String getCategory() { return category; }
    public String getName() { return name; }
    public String getDescription() { return description; }
}
*/
package com.example.job.domain.company.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class WelfareItem {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String category;
    private String name;
    private String description;

    // (이 파일은 순환 참조의 원인이 아니므로 깨끗하게 유지)
}