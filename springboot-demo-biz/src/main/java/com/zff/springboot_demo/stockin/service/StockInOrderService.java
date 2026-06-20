package com.zff.springboot_demo.stockin.service;

import com.zff.springboot_demo.exception.BusinessException;
import com.zff.springboot_demo.exception.ErrorCode;
import com.zff.springboot_demo.stockin.entity.Inventory;
import com.zff.springboot_demo.stockin.entity.StockInOrder;
import com.zff.springboot_demo.stockin.repository.InventoryRepository;
import com.zff.springboot_demo.stockin.repository.StockInOrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class StockInOrderService {

    private final StockInOrderRepository stockInOrderRepository;
    private final InventoryRepository inventoryRepository;

    public StockInOrderService(StockInOrderRepository stockInOrderRepository,
                               InventoryRepository inventoryRepository) {
        this.stockInOrderRepository = stockInOrderRepository;
        this.inventoryRepository = inventoryRepository;
    }

    /** 查详情 */
    public StockInOrder findById(Long id) {
        return stockInOrderRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "入库单不存在"));
    }

    /** 库管员创建入库单：状态 PENDING_RECEIVE */
    public StockInOrder create(Long productId, Integer quantity, Long createdBy) {
        StockInOrder order = new StockInOrder();
        order.setProductId(productId);
        order.setQuantity(quantity);
        order.setCreatedBy(createdBy);
        order.setCreatedAt(LocalDateTime.now());
        return stockInOrderRepository.save(order);
    }

    /** 库管员验收：PENDING_RECEIVE → RECEIVED */
    public StockInOrder receive(Long id) {
        StockInOrder order = findById(id);
        if (!"PENDING_RECEIVE".equals(order.getStatus())) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "只有待收货的入库单才能验收");
        }
        order.setStatus("RECEIVED");
        order.setReceivedAt(LocalDateTime.now());
        return stockInOrderRepository.save(order);
    }

    /**
     * 库管员上架：RECEIVED → SHELVED
     * 关键：状态变更 + 库存累加，两步必须在一个事务里。
     */
    @Transactional
    public StockInOrder shelve(Long id, String location) {
        StockInOrder order = findById(id);
        if (!"RECEIVED".equals(order.getStatus())) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "只有已验收的入库单才能上架");
        }

        // 1. 入库单状态变更
        order.setStatus("SHELVED");
        order.setShelvedAt(LocalDateTime.now());
        stockInOrderRepository.save(order);

        // 2. 库存累加：有则更新，无则新建
        Inventory inventory = inventoryRepository.findByProductId(order.getProductId())
                .orElseGet(() -> {
                    Inventory inv = new Inventory();
                    inv.setProductId(order.getProductId());
                    inv.setQuantity(0);
                    inv.setCreatedAt(LocalDateTime.now());
                    return inv;
                });
        inventory.setLocation(location);
        inventory.setQuantity(inventory.getQuantity() + order.getQuantity());
        inventory.setUpdatedAt(LocalDateTime.now());
        inventoryRepository.save(inventory);

        return order;
    }

    /**
     * 按状态分页查询，数据级权限。
     * ADMIN 看全部；库管员只看自己创建的。
     */
    public Page<StockInOrder> findByStatus(String status, Pageable pageable,
                                           Long currentUserId, boolean isAdmin) {
        boolean hasStatus = status != null && !status.isBlank();
        if (isAdmin) {
            return hasStatus
                    ? stockInOrderRepository.findByStatus(status, pageable)
                    : stockInOrderRepository.findAll(pageable);
        } else {
            return hasStatus
                    ? stockInOrderRepository.findByCreatedByAndStatus(currentUserId, status, pageable)
                    : stockInOrderRepository.findByCreatedBy(currentUserId, pageable);
        }
    }
}
