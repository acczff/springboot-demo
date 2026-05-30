package com.zff.springboot_demo.inspection.service;

import com.zff.springboot_demo.exception.BusinessException;
import com.zff.springboot_demo.exception.ErrorCode;
import com.zff.springboot_demo.inspection.dto.InspectionOrderCreateRequest;
import com.zff.springboot_demo.inspection.dto.InspectionOrderFailRequest;
import com.zff.springboot_demo.inspection.dto.InspectionOrderReviewRequest;
import com.zff.springboot_demo.inspection.entity.InspectionOrder;
import com.zff.springboot_demo.inspection.repository.InspectionOrderRepository;
import com.zff.springboot_demo.user.service.UserService;
import com.zff.springboot_demo.workorder.entity.WorkOrder;
import com.zff.springboot_demo.workorder.service.WorkOrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
     * 按状态分页查询质检单。
     * 数据级权限：ADMIN 看全部；其他角色只看自己（inspectorId = currentUserId）。
     */
    public Page<InspectionOrder> findByStatus(String status, Pageable pageable, Long currentUserId, boolean isAdmin) {
        boolean hasStatus = status != null && !status.isBlank();
        if (isAdmin) {
            // 管理员：看全部
            return hasStatus
                    ? inspectionOrderRepository.findByStatus(status, pageable)
                    : inspectionOrderRepository.findAll(pageable);
        } else {
            // 非管理员：只看自己的
            return hasStatus
                    ? inspectionOrderRepository.findByInspectorIdAndStatus(currentUserId, status, pageable)
                    : inspectionOrderRepository.findByInspectorId(currentUserId, pageable);
        }
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

    /**
     * 发起评审：FAIL → REVIEWING。
     */
    public InspectionOrder startReview(Long id) {
        InspectionOrder order = findById(id);
        if (!"FAIL".equals(order.getStatus())) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "只有不合格的质检单才能发起评审");
        }
        order.setStatus("REVIEWING");
        return inspectionOrderRepository.save(order);
    }

    /**
     * 完成评审：REVIEWING → REVIEWED。
     * 写入评审人、评审意见、处置方式、评审时间。
     */
    public InspectionOrder review(Long id, InspectionOrderReviewRequest request, Long reviewerId) {
        InspectionOrder order = findById(id);
        if (!"REVIEWING".equals(order.getStatus())) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "只有评审中的质检单才能完成评审");
        }
        String reviewerName = userService.findById(reviewerId).getUsername();
        order.setStatus("REVIEWED");
        order.setReviewerId(reviewerId);
        order.setReviewerName(reviewerName);
        order.setReviewOpinion(request.getReviewOpinion());
        order.setDisposal(request.getDisposal());
        order.setReviewedAt(LocalDateTime.now());
        return inspectionOrderRepository.save(order);
    }

    /**
     * 处置执行：REVIEWED → DISPOSED。
     * REWORK 时额外创建一张新的 PENDING 质检单，两步原子执行。
     */
    @Transactional
    public InspectionOrder dispose(Long id) {
        InspectionOrder order = findById(id);
        if (!"REVIEWED".equals(order.getStatus())) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "只有已评审的质检单才能执行处置");
        }
        order.setStatus("DISPOSED");
        inspectionOrderRepository.save(order);

        if ("REWORK".equals(order.getDisposal())) {
            InspectionOrder newOrder = new InspectionOrder();
            newOrder.setWorkOrderId(order.getWorkOrderId());
            newOrder.setCreatedBy(order.getCreatedBy());
            newOrder.setCreatedByName(order.getCreatedByName());
            newOrder.setCreatedTime(LocalDateTime.now());
            // status 默认 PENDING（实体字段默认值）
            inspectionOrderRepository.save(newOrder);
        }
        return order;
    }
}
