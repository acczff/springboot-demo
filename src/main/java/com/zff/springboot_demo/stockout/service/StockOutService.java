package com.zff.springboot_demo.stockout.service;

import com.zff.springboot_demo.exception.BusinessException;
import com.zff.springboot_demo.exception.ErrorCode;
import com.zff.springboot_demo.stockin.entity.Inventory;
import com.zff.springboot_demo.stockin.repository.InventoryRepository;
import com.zff.springboot_demo.stockout.dto.CreateStockOutRequest;
import com.zff.springboot_demo.stockout.entity.StockOutItem;
import com.zff.springboot_demo.stockout.entity.StockOutOrder;
import com.zff.springboot_demo.stockout.repository.StockOutItemRepository;
import com.zff.springboot_demo.stockout.repository.StockOutOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StockOutService {

    private final StockOutOrderRepository orderRepository;
    private final StockOutItemRepository itemRepository;
    private final InventoryRepository inventoryRepository;

    public StockOutService(StockOutOrderRepository orderRepository,
                           StockOutItemRepository itemRepository,
                           InventoryRepository inventoryRepository) {
        this.orderRepository = orderRepository;
        this.itemRepository = itemRepository;
        this.inventoryRepository = inventoryRepository;
    }

    /** 查出库单详情 */
    public StockOutOrder findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "出库单不存在"));
    }

    /** 查出库单列表 */
    public List<StockOutOrder> findAll() {
        return orderRepository.findAll();
    }

    /** 查出库单明细 */
    public List<StockOutItem> findItems(Long orderId) {
        findById(orderId); // 确认出库单存在
        return itemRepository.findByOrderId(orderId);
    }

    /**
     * ADMIN 创建出库单。
     * 第一层检查：创建时预检库存，快速失败（防止带病入库）。
     */
    @Transactional
    public StockOutOrder create(CreateStockOutRequest request, Long createdBy) {
        // 第一层：创建时预检所有产品库存
        for (CreateStockOutRequest.ItemRequest itemReq : request.getItems()) {
            Inventory inv = inventoryRepository.findByProductId(itemReq.getProductId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND,
                            "产品 " + itemReq.getProductId() + " 无库存记录"));
            if (inv.getQuantity() < itemReq.getQuantity()) {
                throw new BusinessException(ErrorCode.BAD_REQUEST,
                        "产品 " + itemReq.getProductId() + " 库存不足，当前库存: " + inv.getQuantity());
            }
        }

        // 建出库单
        StockOutOrder order = new StockOutOrder();
        order.setCreatedBy(createdBy);
        order.setCreatedAt(LocalDateTime.now());
        orderRepository.save(order);

        // 建明细
        for (CreateStockOutRequest.ItemRequest itemReq : request.getItems()) {
            StockOutItem item = new StockOutItem();
            item.setOrderId(order.getId());
            item.setProductId(itemReq.getProductId());
            item.setPlannedQuantity(itemReq.getQuantity());
            itemRepository.save(item);
        }
        return order;
    }

    /**
     * WORKER 拣货：PENDING → PICKING。
     */
    public StockOutOrder pick(Long id, Long pickedBy) {
        StockOutOrder order = findById(id);
        if (!"PENDING".equals(order.getStatus())) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "只有待拣货的出库单才能开始拣货");
        }
        order.setStatus("PICKING");
        order.setPickedBy(pickedBy);
        order.setPickedAt(LocalDateTime.now());
        return orderRepository.save(order);
    }

    /**
     * ADMIN 复核：PICKING → PENDING_REVIEW。
     */
    public StockOutOrder review(Long id, Long reviewedBy, String reviewNote) {
        StockOutOrder order = findById(id);
        if (!"PICKING".equals(order.getStatus())) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "只有拣货中的出库单才能复核");
        }
        order.setStatus("PENDING_REVIEW");
        order.setReviewedBy(reviewedBy);
        order.setReviewedAt(LocalDateTime.now());
        order.setReviewNote(reviewNote);
        return orderRepository.save(order);
    }

    /**
     * ADMIN 确认出库：PENDING_REVIEW → COMPLETED。
     * 第二层检查：事务内再次验证库存，防多单并发抢出。
     * 通过后扣减 inventory.quantity。
     */
    @Transactional
    public StockOutOrder complete(Long id, Long operatorId) {
        StockOutOrder order = findById(id);
        if (!"PENDING_REVIEW".equals(order.getStatus())) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "只有待复核的出库单才能确认出库");
        }

        List<StockOutItem> items = itemRepository.findByOrderId(id);

        // 第二层：事务内再检，防并发漏洞
        for (StockOutItem item : items) {
            Inventory inv = inventoryRepository.findByProductId(item.getProductId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND,
                            "产品 " + item.getProductId() + " 库存记录不存在"));
            if (inv.getQuantity() < item.getPlannedQuantity()) {
                throw new BusinessException(ErrorCode.BAD_REQUEST,
                        "产品 " + item.getProductId() + " 库存不足，无法出库。当前库存: " + inv.getQuantity());
            }
            inv.setQuantity(inv.getQuantity() - item.getPlannedQuantity());
            inventoryRepository.save(inv);
        }

        order.setStatus("COMPLETED");
        order.setCompletedAt(LocalDateTime.now());
        return orderRepository.save(order);
    }
}
