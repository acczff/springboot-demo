package com.zff.springboot_demo.stockin.service;

import com.zff.springboot_demo.stockin.entity.Inventory;
import com.zff.springboot_demo.stockin.repository.InventoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public List<Inventory> findAll() {
        return inventoryRepository.findAll();
    }
}
