package com.example.job.domain.company.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "company_welfare")
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
