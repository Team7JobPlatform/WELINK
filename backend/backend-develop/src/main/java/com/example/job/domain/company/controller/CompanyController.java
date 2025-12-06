package com.example.job.domain.company.controller;

import com.example.job.domain.company.entity.Company;
import com.example.job.domain.company.entity.WelfareItem;
import com.example.job.domain.company.service.CompanyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping
    public ResponseEntity<List<Company>> getAllCompanies() {
        List<Company> companies = companyService.getAllCompanies();
        return ResponseEntity.ok(companies);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable Long id) {
        Company company = companyService.getCompanyById(id);
        return ResponseEntity.ok(company);
    }

    @GetMapping("/{id}/welfares")
    public ResponseEntity<List<WelfareItem>> getWelfaresByCompanyId(@PathVariable Long id) {
        List<WelfareItem> welfares = companyService.getWelfareItemsByCompanyId(id);
        return ResponseEntity.ok(welfares);
    }

    @GetMapping("/location/{location}")
    public ResponseEntity<List<Company>> getCompaniesByLocation(@PathVariable String location) {
        List<Company> companies = companyService.getCompaniesByLocation(location);
        return ResponseEntity.ok(companies);
    }
}
