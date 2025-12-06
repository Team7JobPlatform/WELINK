/*
package com.example.job.domain.company.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "company_welfare")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyWelfare {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    @JsonIgnore  // 추가!
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "welfare_item_id")
    private WelfareItem welfareItem;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Company getCompany() { return company; }
    public void setCompany(Company company) { this.company = company; }

    public WelfareItem getWelfareItem() { return welfareItem; }
    public void setWelfareItem(WelfareItem welfareItem) { this.welfareItem = welfareItem; }
}
*/
package com.example.job.domain.company.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties; // Jackson import

@Entity
@Table(name = "company_welfare")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class CompanyWelfare {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ★★★ 핵심: Company 필드를 무시하여 루프 방지 ★★★
    @JsonIgnoreProperties({"companyWelfares"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    // WelfareItem은 복지 태그 이름이 필요하므로 그대로 둡니다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "welfare_item_id")
    private WelfareItem welfareItem;
}