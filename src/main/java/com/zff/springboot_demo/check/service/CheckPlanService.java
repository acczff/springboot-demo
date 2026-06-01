package com.zff.springboot_demo.check.service;

import com.zff.springboot_demo.check.entity.CheckItem;
import com.zff.springboot_demo.check.entity.InventoryCheckPlan;
import com.zff.springboot_demo.check.repository.CheckItemRepository;
import com.zff.springboot_demo.check.repository.CheckPlanRepository;
import com.zff.springboot_demo.exception.BusinessException;
import com.zff.springboot_demo.exception.ErrorCode;
import com.zff.springboot_demo.stockin.entity.Inventory;
import com.zff.springboot_demo.stockin.repository.InventoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CheckPlanService {

    private final CheckPlanRepository checkPlanRepository;
    private final CheckItemRepository checkItemRepository;
    private final InventoryRepository inventoryRepository;

    public CheckPlanService(CheckPlanRepository checkPlanRepository,
                            CheckItemRepository checkItemRepository,
                            InventoryRepository inventoryRepository) {
        this.checkPlanRepository = checkPlanRepository;
        this.checkItemRepository = checkItemRepository;
        this.inventoryRepository = inventoryRepository;
    }

    /** 查盘点计划详情 */
    public InventoryCheckPlan findById(Long id) {
        return checkPlanRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "盘点计划不存在"));
    }

    /** 查某计划下的全部明细 */
    public List<CheckItem> findItems(Long planId) {
        return checkItemRepository.findByPlanId(planId);
    }

    /** 查全部盘点计划 */
    public List<InventoryCheckPlan> findAll() {
        return checkPlanRepository.findAll();
    }

    /**
     * 主管创建盘点计划：
     * 1. 建盘点计划记录（PENDING）
     * 2. 对每个产品，从 inventory 快照当前 system_quantity，建 check_item
     */
    @Transactional
    public InventoryCheckPlan create(List<Long> productIds, Long createdBy) {
        InventoryCheckPlan plan = new InventoryCheckPlan();
        plan.setCreatedBy(createdBy);
        plan.setCreatedAt(LocalDateTime.now());
        checkPlanRepository.save(plan);

        for (Long productId : productIds) {
            Inventory inventory = inventoryRepository.findByProductId(productId)
                    .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND,
                            "产品 " + productId + " 无库存记录，无法盘点"));

            CheckItem item = new CheckItem();
            item.setPlanId(plan.getId());
            item.setProductId(productId);
            item.setSystemQuantity(inventory.getQuantity()); // 快照
            checkItemRepository.save(item);
        }
        return plan;
    }

    /** 主管开始盘点：PENDING → IN_PROGRESS */
    public InventoryCheckPlan start(Long id) {
        InventoryCheckPlan plan = findById(id);
        if (!"PENDING".equals(plan.getStatus())) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "只有待开始的计划才能开始盘点");
        }
        plan.setStatus("IN_PROGRESS");
        return checkPlanRepository.save(plan);
    }

    /** 盘点员录入实盘数：只在 IN_PROGRESS 状态下允许 */
    public CheckItem recordActual(Long planId, Long itemId, Integer actualQuantity) {
        InventoryCheckPlan plan = findById(planId);
        if (!"IN_PROGRESS".equals(plan.getStatus())) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "盘点计划未处于盘点中状态");
        }
        CheckItem item = checkItemRepository.findById(itemId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "明细不存在"));
        if (!item.getPlanId().equals(planId)) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "明细不属于该盘点计划");
        }
        item.setActualQuantity(actualQuantity);
        return checkItemRepository.save(item);
    }

    /**
     * 盘点员提交：IN_PROGRESS → PENDING_REVIEW
     * 同时计算所有明细的 difference = actual - system
     * 要求所有明细都已录入实盘数，否则不允许提交
     */
    @Transactional
    public InventoryCheckPlan submit(Long id) {
        InventoryCheckPlan plan = findById(id);
        if (!"IN_PROGRESS".equals(plan.getStatus())) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "只有盘点中的计划才能提交");
        }
        List<CheckItem> items = checkItemRepository.findByPlanId(id);
        // 校验所有明细都已录入
        boolean anyNotRecorded = items.stream().anyMatch(i -> i.getActualQuantity() == null);
        if (anyNotRecorded) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "还有明细未录入实盘数，请全部录完再提交");
        }
        // 写入差异
        for (CheckItem item : items) {
            item.setDifference(item.getActualQuantity() - item.getSystemQuantity());
            checkItemRepository.save(item);
        }
        plan.setStatus("PENDING_REVIEW");
        return checkPlanRepository.save(plan);
    }

    /**
     * 主管审核通过：PENDING_REVIEW → COMPLETED
     * 同时用实盘数更新 inventory（事务：计划状态变 + 库存更新）
     */
    @Transactional
    public InventoryCheckPlan review(Long id, Long reviewedBy, String reviewNote) {
        InventoryCheckPlan plan = findById(id);
        if (!"PENDING_REVIEW".equals(plan.getStatus())) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "只有待审核的计划才能审核");
        }
        List<CheckItem> items = checkItemRepository.findByPlanId(id);
        // 用实盘数修正库存
        for (CheckItem item : items) {
            Inventory inventory = inventoryRepository.findByProductId(item.getProductId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND,
                            "产品 " + item.getProductId() + " 库存记录不存在"));
            inventory.setQuantity(item.getActualQuantity());
            inventoryRepository.save(inventory);
        }
        plan.setStatus("COMPLETED");
        plan.setReviewedBy(reviewedBy);
        plan.setReviewedAt(LocalDateTime.now());
        plan.setReviewNote(reviewNote);
        return checkPlanRepository.save(plan);
    }
}
