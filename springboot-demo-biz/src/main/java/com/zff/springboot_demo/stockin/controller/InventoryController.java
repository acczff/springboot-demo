package com.zff.springboot_demo.stockin.controller;

import com.zff.springboot_demo.Result;
import com.zff.springboot_demo.stockin.entity.Inventory;
import com.zff.springboot_demo.stockin.service.InventoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    /** 查询全部库存（需登录） */
    @GetMapping
    public Result<List<Inventory>> findAll() {
        return Result.success("查询成功", inventoryService.findAll());
    }
}
