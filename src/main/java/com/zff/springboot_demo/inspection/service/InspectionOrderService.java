package com.zff.springboot_demo.inspection.service;

import com.zff.springboot_demo.exception.BusinessException;
import com.zff.springboot_demo.exception.ErrorCode;
import com.zff.springboot_demo.inspection.dto.InspectionOrderCreateRequest;
import com.zff.springboot_demo.inspection.dto.InspectionOrderFailRequest;
import com.zff.springboot_demo.inspection.entity.InspectionOrder;
import com.zff.springboot_demo.inspection.repository.InspectionOrderRepository;
import com.zff.springboot_demo.user.service.UserService;
import com.zff.springboot_demo.workorder.entity.WorkOrder;
import com.zff.springboot_demo.workorder.service.WorkOrderService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InspectionOrderService {

    private final InspectionOrderRepository inspectionOrderRepository;
    private final WorkOrderService workOrderService;
    private final UserService userService;

    public InspectionOrderService(InspectionOrderRepository inspectionOrderRepository,
                                  WorkOrderService workOrderService,
                                  UserService userService) {
        this.inspectionOrderRepository = inspectionOrderRepository;
        this.workOrderService = workOrderService;
        this.userService = userService;
    }

    /** 查询质检单详情 */
    public InspectionOrder findById(Long id) {
        return inspectionOrderRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "质检单不存在"));
    }

    /** 查询工单下所有质检单 */
    public List<InspectionOrder> findByWorkOrderId(Long workOrderId) {
        return inspectionOrderRepository.findByWorkOrderId(workOrderId);
    }

    /**
     * 创建质检单。
     * 业务规则：只有 COMPLETED 状态的工单才能创建质检单。
     */
    public InspectionOrder create(InspectionOrderCreateRequest request, Long createdBy) {
        // 校验工单存在且已完成
        WorkOrder workOrder = workOrderService.findById(request.getWorkOrderId());
        if (!"COMPLETED".equals(workOrder.getStatus())) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "只有已完成的工单才能创建质检单");
        }

        // 取创建人姓名快照
        String createdByName = userService.findById(createdBy).getUsername();

        InspectionOrder order = new InspectionOrder();
        order.setWorkOrderId(request.getWorkOrderId());
        order.setCreatedBy(createdBy);
        order.setCreatedByName(createdByName);
        order.setCreatedTime(LocalDateTime.now());
        return inspectionOrderRepository.save(order);
    }

    /**
     * 开始检验：PENDING → INSPECTING。
     * 检验人由当前登录用户担任。
     */
    public InspectionOrder start(Long id, Long inspectorId) {
        InspectionOrder order = findById(id);
        if (!"PENDING".equals(order.getStatus())) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "只有待检状态的质检单才能开始检验");
        }
        String inspectorName = userService.findById(inspectorId).getUsername();
        order.setStatus("INSPECTING");
        order.setInspectorId(inspectorId);
        order.setInspectorName(inspectorName);
        return inspectionOrderRepository.save(order);
    }

    /**
     * 检验通过：INSPECTING → PASS。
     */
    public InspectionOrder pass(Long id) {
        InspectionOrder order = findById(id);
        if (!"INSPECTING".equals(order.getStatus())) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "只有检验中的质检单才能判定结果");
        }
        order.setStatus("PASS");
        order.setInspectedAt(LocalDateTime.now());
        return inspectionOrderRepository.save(order);
    }

    /**
     * 检验不合格：INSPECTING → FAIL。
     * 必须填写不合格原因。
     */
    public InspectionOrder fail(Long id, InspectionOrderFailRequest request) {
        InspectionOrder order = findById(id);
        if (!"INSPECTING".equals(order.getStatus())) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "只有检验中的质检单才能判定结果");
        }
        order.setStatus("FAIL");
        order.setFailReason(request.getFailReason());
        order.setInspectedAt(LocalDateTime.now());
        return inspectionOrderRepository.save(order);
    }
}
