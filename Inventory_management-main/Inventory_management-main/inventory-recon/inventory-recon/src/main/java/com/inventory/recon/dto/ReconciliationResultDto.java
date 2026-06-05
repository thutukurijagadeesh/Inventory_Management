package com.inventory.recon.dto;

public class ReconciliationResultDto {

    private String sku;

    private int totalIn;

    private int totalOut;

    private int netStock;

    private String status;

    // =====================================
    // NEW FIELD
    // =====================================

    private String reason;

    // =====================================
    // GETTERS & SETTERS
    // =====================================

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public int getTotalIn() {
        return totalIn;
    }

    public void setTotalIn(int totalIn) {
        this.totalIn = totalIn;
    }

    public int getTotalOut() {
        return totalOut;
    }

    public void setTotalOut(int totalOut) {
        this.totalOut = totalOut;
    }

    public int getNetStock() {
        return netStock;
    }

    public void setNetStock(int netStock) {
        this.netStock = netStock;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // =====================================
    // REASON
    // =====================================

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}