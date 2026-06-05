package com.inventory.recon.dto;

import java.util.List;

public class UploadResponse {

    private String message;

    private int successCount;

    private int failedCount;

    private List<Integer> failedRows;

    private String batchId;

    public UploadResponse(
            String message,
            int successCount,
            int failedCount,
            List<Integer> failedRows,
            String batchId
    ) {
        this.message = message;
        this.successCount = successCount;
        this.failedCount = failedCount;
        this.failedRows = failedRows;
        this.batchId = batchId;
    }

    public String getMessage() {
        return message;
    }

    public int getSuccessCount() {
        return successCount;
    }

    public int getFailedCount() {
        return failedCount;
    }

    public List<Integer> getFailedRows() {
        return failedRows;
    }

    public String getBatchId() {
        return batchId;
    }
}