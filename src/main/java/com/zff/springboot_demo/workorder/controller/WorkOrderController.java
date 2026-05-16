package com.zff.springboot_demo.workorder.controller;

import com.zff.springboot_demo.Result;
import com.zff.springboot_demo.dto.PageResult;
import com.zff.springboot_demo.workorder.dto.WorkOrderCreateRequest;
import com.zff.springboot_demo.workorder.entity.Product;
import com.zff.springboot_demo.workorder.entity.WorkOrder;
import com.zff.springboot_demo.workorder.entity.WorkOrderItem;
import com.zff.springboot_demo.workorder.service.ProductService;
import com.zff.springboot_demo.workorder.service.WorkOrderItemService;
import com.zff.springboot_demo.workorder.service.WorkOrderService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

/**
 * 工单管理接口预留类，后续用于承载工单相关 HTTP API。
 */

@RestController
@RequestMapping("/api/workorders")
public class WorkOrderController {

    private final WorkOrderService workOrderService;
    private final ProductService productService;
    private final WorkOrderItemService workOrderItemService;

    public WorkOrderController(WorkOrderService workOrderService, ProductService productService, WorkOrderItemService workOrderItemService) {
        this.workOrderService = workOrderService;
        this.productService = productService;
        this.workOrderItemService = workOrderItemService;
    }

    @GetMapping
    public Result<PageResult<WorkOrder>> findAll(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String status
    ) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page<WorkOrder> page = workOrderService.findByStatus(status, pageable);
        PageResult<WorkOrder> pageResult = new PageResult<>(page.getContent(), page.getTotalElements());
        return Result.success("查询成功", pageResult);
    }

    @GetMapping("/{id}")
    public Result<WorkOrder> findById(Long id) {
        WorkOrder workOrder = workOrderService.findById(id);
        return Result.success("查询成功", workOrder);
    }

    @PostMapping
    public Result<WorkOrder> create(@RequestBody @Valid WorkOrderCreateRequest request) {
        WorkOrder workOrder = new WorkOrder();
        workOrder.setCreatedTime(System.currentTimeMillis());
        workOrder.setStatus("DRAFT");
        workOrder.setCreatedBy(request.getCreatedBy());
        workOrderService.create(workOrder);
        request.getWorkOrderItemRequests().forEach(item -> {
            Product product =  productService.findById(item.getProductId());
            WorkOrderItem  workOrderItem = new WorkOrderItem();
            workOrderItem.setWorkOrder(workOrder);
            workOrderItem.setProduct(product);
            workOrderItem.setPlannedQty(item.getPlannedQty());
            workOrderItemService.save(workOrderItem);
        });
        return Result.success("创建成功",workOrder);
    }

    @PutMapping("/{id}/issue")
    public Result<WorkOrder> issue(@PathVariable Long id) {
    // Service 内部会抛异常，这里完全不需要判断
    return Result.success("下发成功", workOrderService.issue(id));
}
}
