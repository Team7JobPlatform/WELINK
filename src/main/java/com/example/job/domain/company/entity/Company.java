/*
package com.example.job.domain.company.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "company")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String location;
    private String industry;    // 업종 (IT, 제조 등)

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    @JsonIgnore  // 추가!
    private List<CompanyWelfare> companyWelfares = new ArrayList<>();

    // Getters and Setters (그대로 유지)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public List<CompanyWelfare> getCompanyWelfares() { return companyWelfares; }
    public void setCompanyWelfares(List<CompanyWelfare> companyWelfares) { this.companyWelfares = companyWelfares; }

    public String getIndustry() { return industry; }
    public void setIndustry(String industry) { this.industry = industry; }
}
*//*

package com.example.job.domain.company.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties; // Jackson import

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Company {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String location;
    private String industry;
    private String description;

    @ManyToMany(fetch = FetchType.LAZY)
    // ★★★ 핵심: 순환 참조 방지용 어노테이션 추가 ★★★
    @JsonIgnoreProperties({"company"}) // CompanyWelfare에서 다시 Company를 참조하는 것을 막음
    @JoinTable(
            name = "company_welfare",
            joinColumns = @JoinColumn(name = "company_id"),
            inverseJoinColumns = @JoinColumn(name="welfare_item_id")
    )
    private Set<CompanyWelfare> companyWelfares;
    private Set<WelfareItem> welfareItems = new HashSet<>();
}*/
package com.example.job.domain.company.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
public class Company {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String location;
    private String industry;

    // ★★★ [태그 검색용 필드 추가] ★★★
    // 이 필드가 없으면 CompanyRepository에서 에러가 날 수 있습니다.
    private String tags;

    // ★★★ [핵심 수정] 이 부분의 어노테이션이 없으면 오류가 납니다! ★★★
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "company_welfare",
            joinColumns = @JoinColumn(name = "company_id"),
            inverseJoinColumns = @JoinColumn(name = "welfare_item_id")
    )
    private Set<WelfareItem> welfareItems = new HashSet<>();

    // 생성자 추가 (DataInit에서 사용)
    public Company(String name, String description, String location, String industry, String tags) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.industry = industry;
        this.tags = tags;
    }
}