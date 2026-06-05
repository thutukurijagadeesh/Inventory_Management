package com.inventory.recon.util;

import com.inventory.recon.dto.ParsedResult;
import com.inventory.recon.dto.UploadResponse;

import com.inventory.recon.model.StockMovement;

import com.opencsv.CSVReader;

import org.springframework.stereotype.Component;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;

import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class CsvParser {

    public ParsedResult parse(
            MultipartFile file
    ) throws Exception {

        // =====================================
        // FILE VALIDATION
        // =====================================

        if (
                file == null
                        ||
                        file.isEmpty()
        ) {

            throw new RuntimeException(
                    "CSV file is empty"
            );
        }

        if (
                !file.getOriginalFilename()
                        .toLowerCase()
                        .endsWith(".csv")
        ) {

            throw new RuntimeException(
                    "Only CSV files are allowed"
            );
        }

        // =====================================
        // STORAGE
        // =====================================

        List<StockMovement> validRows =
                new ArrayList<>();

        List<Integer> failedRows =
                new ArrayList<>();

        String batchId =
                UUID.randomUUID().toString();

        int successCount = 0;

        int rowNumber = 1;

        CSVReader reader =
                new CSVReader(
                        new InputStreamReader(
                                file.getInputStream()
                        )
                );

        // =====================================
        // HEADER VALIDATION
        // =====================================

        String[] header =
                reader.readNext();

        if (header == null) {

            throw new RuntimeException(
                    "CSV file has no header"
            );
        }

        String actualHeader =
                String.join(",", header)
                        .replace(" ", "")
                        .toLowerCase();

// =====================================
// ACCEPTED CSV FORMATS
// =====================================

        boolean validHeader =

                actualHeader.equals(
                        "sku,quantity,movementtype,source,movementdate"
                )

                        ||

                        actualHeader.equals(
                                "sku,quantity,type,source,date"
                        );

// =====================================
// INVALID STRUCTURE
// =====================================

        if (!validHeader) {

            throw new RuntimeException(

                    "Invalid CSV structure.\n\n"

                            +

                            "Accepted formats:\n"

                            +

                            "1. sku,quantity,movementType,source,movementDate\n"

                            +

                            "2. sku,quantity,type,source,date"
            );
        }

        // =====================================
        // PROCESS ROWS
        // =====================================

        String[] row;

        while ((row = reader.readNext()) != null) {

            rowNumber++;

            try {

                // Column count validation

                if (row.length != 5) {

                    failedRows.add(rowNumber);

                    continue;
                }

                // Read values safely

                String sku =
                        row[0] != null
                                ? row[0].trim()
                                : "";

                String quantityText =
                        row[1] != null
                                ? row[1].trim()
                                : "";

                String movementType =
                        row[2] != null
                                ? row[2].trim()
                                : "";

                String source =
                        row[3] != null
                                ? row[3].trim()
                                : "";

                String dateText =
                        row[4] != null
                                ? row[4].trim()
                                : "";

                // Empty validation

                if (
                        sku.isBlank()
                                ||
                                quantityText.isBlank()
                                ||
                                movementType.isBlank()
                                ||
                                source.isBlank()
                                ||
                                dateText.isBlank()
                ) {

                    failedRows.add(rowNumber);

                    continue;
                }

                // SKU validation

                if (!sku.startsWith("SKU")) {

                    failedRows.add(rowNumber);

                    continue;
                }

                // Quantity validation

                int quantity;

                try {

                    quantity =
                            Integer.parseInt(
                                    quantityText
                            );

                } catch (Exception e) {

                    failedRows.add(rowNumber);

                    continue;
                }

                if (quantity < 0) {

                    failedRows.add(rowNumber);

                    continue;
                }

                // Movement type validation

                if (
                        !movementType.equalsIgnoreCase("IN")
                                &&
                                !movementType.equalsIgnoreCase("OUT")
                ) {

                    failedRows.add(rowNumber);

                    continue;
                }

                // Source validation

                if (
                        !source.equalsIgnoreCase("PURCHASE")
                                &&
                                !source.equalsIgnoreCase("SALE")
                                &&
                                !source.equalsIgnoreCase("RETURN")
                ) {

                    failedRows.add(rowNumber);

                    continue;
                }

                // Date validation

                LocalDate movementDate;

                try {

                    movementDate =
                            LocalDate.parse(
                                    dateText
                            );

                } catch (Exception e) {

                    failedRows.add(rowNumber);

                    continue;
                }

                // =====================================
                // CREATE ENTITY
                // =====================================

                StockMovement stock =
                        new StockMovement();

                stock.setSku(sku);

                stock.setQuantity(quantity);

                stock.setMovementType(
                        movementType
                );

                stock.setSource(source);

                stock.setMovementDate(
                        movementDate
                );

                stock.setBatchId(batchId);

                stock.setUploadDate(
                        LocalDate.now()
                );

                stock.setOpeningBalance(100);

                validRows.add(stock);

                successCount++;

            } catch (Exception e) {

                failedRows.add(rowNumber);
            }
        }

        // =====================================
        // FINAL RESPONSE
        // =====================================

        String message =
                successCount +
                        " rows uploaded successfully, "
                        +
                        failedRows.size()
                        +
                        " rows failed due to invalid formatting.";

        UploadResponse response =
                new UploadResponse(
                        message,
                        successCount,
                        failedRows.size(),
                        failedRows,
                        batchId
                );

        return new ParsedResult(
                validRows,
                response
        );
    }
}