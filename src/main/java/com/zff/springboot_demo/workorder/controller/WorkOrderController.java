package com.zff.springboot_demo.workorder.controller;

import com.zff.springboot_demo.Result;
import com.zff.springboot_demo.dto.PageResult;
import com.zff.springboot_demo.workorder.dto.WorkOrderCreateRequest;
import com.zff.springboot_demo.workorder.entity.WorkOrder;
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
@RequestMapping("/api/work-orders")
public class WorkOrderController {

    private final WorkOrderService workOrderService;

    public WorkOrderController(WorkOrderService workOrderService) {
        this.workOrderService = workOrderService;
    }

    /**
     * 分页查询工单，可按状态筛选。
     * @param pageNum 页码（从 1 开始）
     * @param pageSize 每页条数
     * @param status 工单状态（可选）
     * @return 工单分页结果
     */
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

    /**
     * 按工单 ID 查询详情。
     * @param id 工单主键
     * @return 工单详情
     */
    @GetMapping("/{id}")
    public Result<WorkOrder> findById(@PathVariable Long id) {
        WorkOrder workOrder = workOrderService.findById(id);
        return Result.success("查询成功", workOrder);
    }

    /**
     * 创建工单（含工单明细）。
     * @param request 创建请求参数
     * @return 创建后的工单
     */
    @PostMapping
    public Result<WorkOrder> create(@RequestBody @Valid WorkOrderCreateRequest request) {
        return Result.success("创建成功", workOrderService.createWithItems(request));
    }

    /**
     * 下发工单。
     * @param id 工单主键
     * @return 下发后的工单
     */
    @PutMapping("/{id}/issue")
    public Result<WorkOrder> issue(@PathVariable Long id) {
        // Service 内部会抛异常，这里完全不需要判断
        return Result.success("下发成功", workOrderService.issue(id));
    }

    /**
     * 软删除工单（同时级联软删除该工单下所有报工记录）。
     * @param id 工单主键
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        workOrderService.softDelete(id);
        return Result.success("删除成功");
    }
}
