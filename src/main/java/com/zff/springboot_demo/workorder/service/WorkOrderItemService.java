package com.zff.springboot_demo.workorder.service;

import com.zff.springboot_demo.workorder.entity.WorkOrderItem;
import com.zff.springboot_demo.workorder.repository.WorkOrderItemRepository;
import org.springframework.stereotype.Service;

@Service
public class WorkOrderItemService {

    private final WorkOrderItemRepository workOrderItemRepository;

    public WorkOrderItemService(WorkOrderItemRepository workOrderItemRepository) {
        this.workOrderItemRepository = workOrderItemRepository;
    }

    public WorkOrderItem save(WorkOrderItem workOrderItem) {
        return workOrderItemRepository.save(workOrderItem);
    }
}
