package com.inventory.recon.dto;

import com.inventory.recon.model.StockMovement;

import java.util.List;

public class ParsedResult {

    private List<StockMovement> stockMovements;

    private UploadResponse uploadResponse;

    public ParsedResult(
            List<StockMovement> stockMovements,
            UploadResponse uploadResponse
    ) {
        this.stockMovements = stockMovements;
        this.uploadResponse = uploadResponse;
    }

    public List<StockMovement> getStockMovements() {
        return stockMovements;
    }

    public UploadResponse getUploadResponse() {
        return uploadResponse;
    }
}