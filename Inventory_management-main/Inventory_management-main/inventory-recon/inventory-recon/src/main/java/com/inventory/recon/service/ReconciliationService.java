package com.inventory.recon.service;

import com.inventory.recon.dto.ReconciliationResultDto;

import com.inventory.recon.model.StockMovement;

import com.inventory.recon.repository.StockMovementRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import java.util.stream.Collectors;

@Service
public class ReconciliationService {

    private final StockMovementRepository repository;

    public ReconciliationService(
            StockMovementRepository repository
    ) {

        this.repository = repository;
    }

    // =========================================
    // RECONCILIATION ENGINE
    // =========================================

    public List<ReconciliationResultDto> reconcile(
            LocalDate date
    ) {

        // =========================================
        // FETCH DATA BY DATE
        // =========================================

        List<StockMovement> data =
                repository.findByMovementDate(date);

        // =========================================
        // GROUP BY SKU
        // =========================================

        Map<String, List<StockMovement>> grouped =

                data.stream()

                        .collect(
                                Collectors.groupingBy(
                                        StockMovement::getSku
                                )
                        );

        // =========================================
        // FINAL RESULTS
        // =========================================

        List<ReconciliationResultDto> results =
                new ArrayList<>();

        // =========================================
        // PROCESS EACH SKU
        // =========================================

        for (String sku : grouped.keySet()) {

            int totalIn = 0;

            int totalOut = 0;

            int openingBalance = 100;

            // =====================================
            // CALCULATE TOTAL IN & OUT
            // =====================================

            for (StockMovement sm : grouped.get(sku)) {

                if (
                        "IN".equalsIgnoreCase(
                                sm.getMovementType()
                        )
                ) {

                    totalIn += sm.getQuantity();
                }

                else if (
                        "OUT".equalsIgnoreCase(
                                sm.getMovementType()
                        )
                ) {

                    totalOut += sm.getQuantity();
                }
            }

            // =====================================
            // CALCULATE CLOSING STOCK
            // =====================================

            int closingStock =
                    openingBalance
                            +
                            totalIn
                            -
                            totalOut;

            // =====================================
            // CREATE DTO
            // =====================================

            ReconciliationResultDto dto =
                    new ReconciliationResultDto();

            dto.setSku(sku);

            dto.setTotalIn(totalIn);

            dto.setTotalOut(totalOut);

            dto.setNetStock(closingStock);

            // =====================================
            // BUSINESS RECONCILIATION RULES
            // =====================================

            String status;

            String reason;

            // CASE 1:
            // OUT GREATER THAN AVAILABLE STOCK

            if (closingStock < 0) {

                status = "MISMATCH";

                reason =
                        "OUT exceeds available stock";

            }

            // CASE 2:
            // STOCK OUT > STOCK IN

            else if (totalOut > totalIn) {

                status = "MISMATCH";

                reason =
                        "Stock OUT greater than Stock IN";
            }

            // CASE 3:
            // PERFECT BALANCE

            else if (closingStock == 0) {

                status = "MATCH";

                reason =
                        "Inventory balanced";
            }

            // CASE 4:
            // STOCK AVAILABLE

            else {

                status = "MATCH";

                reason =
                        "Stock available";
            }

            // =====================================
            // SET STATUS & REASON
            // =====================================

            dto.setStatus(status);

            dto.setReason(reason);

            // =====================================
            // ADD TO RESULTS
            // =====================================

            results.add(dto);
        }

        // =========================================
        // RETURN FINAL RESULTS
        // =========================================

        return results;
    }
}