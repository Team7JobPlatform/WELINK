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
