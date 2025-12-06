package com.example.job.domain.company.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "company")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String location;

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
}
