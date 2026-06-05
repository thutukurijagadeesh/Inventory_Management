package com.inventory.recon.repository;

import com.inventory.recon.model.StockMovement;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

import java.util.List;

public interface StockMovementRepository
        extends JpaRepository<StockMovement, Long> {

    // =========================================
    // FIND BY DATE
    // =========================================

    List<StockMovement> findByMovementDate(
            LocalDate movementDate
    );

    // =========================================
    // DELETE ALL RECORDS
    // =========================================

    void deleteAll();
}