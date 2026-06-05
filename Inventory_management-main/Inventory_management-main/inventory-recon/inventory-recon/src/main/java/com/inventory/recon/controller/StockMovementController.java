package com.inventory.recon.controller;

import com.inventory.recon.dto.ParsedResult;

import com.inventory.recon.model.StockMovement;

import com.inventory.recon.service.StockMovementService;

import com.inventory.recon.util.CsvParser;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "*")

@RestController

@RequestMapping("/api/stock")

public class StockMovementController {

    private final StockMovementService stockMovementService;

    private final CsvParser csvParser;

    public StockMovementController(
            StockMovementService stockMovementService,
            CsvParser csvParser
    ) {

        this.stockMovementService =
                stockMovementService;

        this.csvParser = csvParser;
    }

    // =====================================
    // ADD SINGLE STOCK
    // =====================================

    @PostMapping("/add")

    public StockMovement addStock(
            @RequestBody StockMovement stockMovement
    ) {

        return stockMovementService.save(
                stockMovement
        );
    }

    // =====================================
    // CSV UPLOAD
    // =====================================

    @PostMapping("/upload")

    public ResponseEntity<?> uploadCsv(
            @RequestParam("file")
            MultipartFile file
    ) {

        try {

            // =====================================
            // CLEAR PREVIOUS DATA
            // =====================================

            stockMovementService.clearOldUploadData();

            // =====================================
            // PARSE CSV
            // =====================================

            ParsedResult parsedResult =
                    csvParser.parse(file);

            // =====================================
            // SAVE VALID ROWS ONLY
            // =====================================

            stockMovementService.saveAll(
                    parsedResult.getStockMovements()
            );

            // =====================================
            // RETURN SUCCESS RESPONSE
            // =====================================

            return ResponseEntity.ok(
                    parsedResult.getUploadResponse()
            );

        } catch (Exception e) {

            // =====================================
            // RETURN FRIENDLY ERROR
            // =====================================

            return ResponseEntity.badRequest().body(
                    e.getMessage()
            );
        }
    }
}