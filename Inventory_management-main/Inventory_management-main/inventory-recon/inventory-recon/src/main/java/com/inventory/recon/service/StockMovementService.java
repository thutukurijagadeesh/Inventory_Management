package com.inventory.recon.service;

import com.inventory.recon.model.StockMovement;

import com.inventory.recon.repository.StockMovementRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockMovementService {

    private final StockMovementRepository repository;

    public StockMovementService(
            StockMovementRepository repository
    ) {
        this.repository = repository;
    }

    // =====================================
    // SAVE SINGLE
    // =====================================

    public StockMovement save(
            StockMovement stockMovement
    ) {

        return repository.save(stockMovement);
    }

    // =====================================
    // SAVE ALL
    // =====================================

    public List<StockMovement> saveAll(
            List<StockMovement> stockMovements
    ) {

        return repository.saveAll(stockMovements);
    }

    // =====================================
    // CLEAR PREVIOUS CSV DATA
    // =====================================

    public void clearOldUploadData() {

        repository.deleteAll();
    }
}