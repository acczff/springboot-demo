package com.zff.springboot_demo.workorder.service;

import com.zff.springboot_demo.workorder.entity.WorkOrder;
import com.zff.springboot_demo.workorder.repository.WorkOrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * 工单业务逻辑层，负责工单查询、保存和下发。
 */
@Service
public class WorkOrderService {

    private final WorkOrderRepository workOrderRepository;

    public WorkOrderService(WorkOrderRepository workOrderRepository) {
        this.workOrderRepository = workOrderRepository;
    }

    /**
     * 按状态分页查询工单；状态为空时查询全部。
     */
    public Page<WorkOrder> findByStatus(String status, Pageable pageable) {
        if(status == null || status.isBlank()){
            return workOrderRepository.findAll(pageable);
        }
        return workOrderRepository.findByStatus(status, pageable);
    }

    public WorkOrder findById(Long id) {
        return workOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("工单不存在"));
    }

    /**
     * 创建工单。
     */
    public WorkOrder create(WorkOrder workOrder) {
        return workOrderRepository.save(workOrder);
    }

    /**
     * 更新工单。
     */
    public WorkOrder update(WorkOrder workOrder) {
        return workOrderRepository.save(workOrder);
    }

    /**
     * 下发草稿工单，只有 DRAFT 状态允许下发。
     */
    public WorkOrder issue(Long id) {
        WorkOrder workOrder = this.findById(id);
        if (!"DRAFT".equals(workOrder.getStatus())) {
            throw new RuntimeException("只有草稿状态的工单才能下发");
        }
        workOrder.setStatus("ISSUED");
        return workOrderRepository.save(workOrder);
    }
}
