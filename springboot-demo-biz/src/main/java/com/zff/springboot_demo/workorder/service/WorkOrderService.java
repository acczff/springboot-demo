package com.zff.springboot_demo.workorder.service;

import com.zff.springboot_demo.exception.BusinessException;
import com.zff.springboot_demo.exception.ErrorCode;

import com.zff.springboot_demo.user.entity.User;
import com.zff.springboot_demo.user.service.UserService;
import com.zff.springboot_demo.workorder.dto.WorkOrderCreateRequest;
import com.zff.springboot_demo.workorder.entity.Product;
import com.zff.springboot_demo.workorder.entity.WorkOrder;
import com.zff.springboot_demo.workorder.entity.WorkOrderItem;
import com.zff.springboot_demo.workorder.repository.WorkOrderRepository;
import com.zff.springboot_demo.workreport.service.WorkReportService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 工单业务逻辑层，负责工单查询、保存和下发。
 */
@Service
public class WorkOrderService {

    private final WorkOrderRepository workOrderRepository;
    private final ProductService productService;
    private final WorkOrderItemService workOrderItemService;
    private final UserService userService;
    private final WorkReportService workReportService;

    public WorkOrderService(WorkOrderRepository workOrderRepository,
                            ProductService productService,
                            WorkOrderItemService workOrderItemService,
                            UserService userService,
                            WorkReportService workReportService) {
        this.workOrderRepository = workOrderRepository;
        this.productService = productService;
        this.workOrderItemService = workOrderItemService;
        this.userService = userService;
        this.workReportService = workReportService;
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
     * 下发草稿工单，只有 DRAFT 状态 + 创建人/ADMIN 允许下发。
     */
    public WorkOrder issue(Long id, Long currentUserId) {
        WorkOrder workOrder = this.findById(id);
        checkOwnership(workOrder, currentUserId);
        if (!"DRAFT".equals(workOrder.getStatus())) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "只有草稿状态的工单才能下发");
        }
        workOrder.setStatus("ISSUED");
        return workOrderRepository.save(workOrder);
    }

    @Transactional
    public WorkOrder createWithItems(WorkOrderCreateRequest request) {
        User creator = userService.findById(request.getCreatedBy());
        WorkOrder workOrder = new WorkOrder();
        workOrder.setName(request.getName());
        workOrder.setCreatedTime(System.currentTimeMillis());
        workOrder.setStatus("DRAFT");
        workOrder.setCreatedByName(creator.getUsername());
        this.create(workOrder);
        request.getWorkOrderItemRequests().forEach(item -> {
            Product product =  productService.findById(item.getProductId());
            WorkOrderItem workOrderItem = new WorkOrderItem();
            workOrderItem.setWorkOrder(workOrder);
            workOrderItem.setProduct(product);
            workOrderItem.setPlannedQty(item.getPlannedQty());
            workOrderItemService.save(workOrderItem);
        });
        return workOrder;
    }

    @Transactional
    public void softDelete(Long id, Long currentUserId) {
        WorkOrder workOrder = this.findById(id);
        checkOwnership(workOrder, currentUserId);
        workOrderRepository.deleteById(id); // JPA 会自动拦截并执行 @SQLDelete
        workReportService.softDeleteByWorkOrderId(id);
    }

    public WorkOrder start(Long id, Long currentUserId) {
        WorkOrder workOrder = this.findById(id);
        checkOwnership(workOrder, currentUserId);
        if (!"ISSUED".equals(workOrder.getStatus())) {
            throw new BusinessException(ErrorCode.BAD_REQUEST,
                    "只有已下发状态的工单才能开始生产，当前状态：" + workOrder.getStatus());
        }
        workOrder.setStatus("IN_PROGRESS");
        return workOrderRepository.save(workOrder);
    }

    public WorkOrder complete(Long id, Long currentUserId) {
        WorkOrder workOrder = this.findById(id);
        checkOwnership(workOrder, currentUserId);
        if (!"IN_PROGRESS".equals(workOrder.getStatus())) {
            throw new BusinessException(ErrorCode.BAD_REQUEST,
                    "只有生产中的工单才能完工，当前状态：" + workOrder.getStatus());
        }
        workOrder.setStatus("COMPLETED");
        return workOrderRepository.save(workOrder);
    }

    public WorkOrder cancel(Long id, Long currentUserId) {
        WorkOrder workOrder = this.findById(id);
        checkOwnership(workOrder, currentUserId);
        if (!"ISSUED".equals(workOrder.getStatus()) && !"IN_PROGRESS".equals(workOrder.getStatus())) {
            throw new BusinessException(ErrorCode.BAD_REQUEST,
                    "只有已下发或生产中的工单才能取消，当前状态：" + workOrder.getStatus());
        }
        workOrder.setStatus("CANCELLED");
        return workOrderRepository.save(workOrder);
    }

    /**
     * 越权检查：只有工单创建人或 ADMIN 才能操作。
     * 防止水平越权（用户 A 操作用户 B 的工单）。
     */
    private void checkOwnership(WorkOrder workOrder, Long currentUserId) {
        if (!workOrder.getCreatedBy().equals(currentUserId) && !userService.isAdmin(currentUserId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "无权操作此工单");
        }
    }
}
