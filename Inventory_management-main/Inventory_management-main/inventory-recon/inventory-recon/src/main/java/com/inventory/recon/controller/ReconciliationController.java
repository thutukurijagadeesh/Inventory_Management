package com.inventory.recon.controller;

import com.inventory.recon.dto.ReconciliationResultDto;
import com.inventory.recon.service.ReconciliationService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/reconciliation")
public class ReconciliationController {

    private final ReconciliationService service;

    public ReconciliationController(ReconciliationService service) {
        this.service = service;
    }

    // ✅ Full reconciliation
    @GetMapping
    public List<ReconciliationResultDto> reconcile(@RequestParam String date) {
        return service.reconcile(LocalDate.parse(date));
    }

    // ✅ Only mismatches (Exception Dashboard)
    @GetMapping("/exceptions")
    public List<ReconciliationResultDto> exceptions(@RequestParam String date) {
        return service.reconcile(LocalDate.parse(date))
                .stream()
                .filter(r -> "MISMATCH".equals(r.getStatus()))
                .toList();
    }
}