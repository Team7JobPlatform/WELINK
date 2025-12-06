/*
package com.example.job.domain.company.controller;

import com.example.job.domain.company.entity.WelfareItem;
import com.example.job.domain.company.service.WelfareItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/welfare-items")
public class WelfareItemController {

    private final WelfareItemService welfareItemService;

    public WelfareItemController(WelfareItemService welfareItemService) {
        this.welfareItemService = welfareItemService;
    }

    @GetMapping
    public ResponseEntity<List<WelfareItem>> getAllWelfareItems() {
        List<WelfareItem> welfareItems = welfareItemService.getAllWelfareItems();
        return ResponseEntity.ok(welfareItems);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<WelfareItem>> getWelfareItemsByCategory(@PathVariable String category) {
        List<WelfareItem> welfareItems = welfareItemService.getWelfareItemsByCategory(category);
        return ResponseEntity.ok(welfareItems);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WelfareItem> getWelfareItemById(@PathVariable Long id) {
        WelfareItem welfareItem = welfareItemService.getWelfareItemById(id);
        return ResponseEntity.ok(welfareItem);
    }
}
*//*

package com.example.job.domain.company.controller;

import com.example.job.domain.company.entity.WelfareItem;
import com.example.job.domain.company.service.WelfareItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
// ★★★ 이 부분을 /api/welfare-items 로 수정했습니다! ★★★
@RequestMapping("/api/welfare-items")
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
public class WelfareItemController {

    private final WelfareItemService welfareService;

    public WelfareItemController(WelfareItemService welfareService) {
        this.welfareService = welfareService;
    }

    @GetMapping
    public ResponseEntity<List<WelfareItem>> getAllWelfares() {
        List<WelfareItem> items = welfareService.getAllWelfareItems();
        return ResponseEntity.ok(items);
    }
}*/
package com.example.job.domain.company.controller;

import com.example.job.domain.company.entity.Company;
import com.example.job.domain.company.service.WelfareItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/welfare-items")
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
public class WelfareItemController {

    private final WelfareItemService welfareitemService;

    public WelfareItemController(WelfareItemService welfareService) {
        this.welfareitemService = welfareService;
    }

    @GetMapping
    // ★ 반환 타입을 CompanyWelfare로 변경했습니다.
    public ResponseEntity<List<Company>> getAllWelfares() {
        List<Company> items = welfareitemService.getAllWelfareItems();
        return ResponseEntity.ok(items);
    }
}
