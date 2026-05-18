package com.zff.springboot_demo.workreport.service;

import com.zff.springboot_demo.user.entity.User;
import com.zff.springboot_demo.user.service.UserService;
import com.zff.springboot_demo.workorder.entity.WorkOrderItem;
import com.zff.springboot_demo.workorder.repository.WorkOrderItemRepository;
import com.zff.springboot_demo.workreport.dto.WorkReportCreateRequest;
import com.zff.springboot_demo.workreport.entity.WorkReport;
import com.zff.springboot_demo.workreport.repository.WorkReportRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WorkReportService {

    private final WorkReportRepository workReportRepository;
    private final UserService  userService;
    private final WorkOrderItemRepository workOrderItemRepository;

    public WorkReportService(WorkReportRepository workReportRepository, UserService userService,
                             WorkOrderItemRepository workOrderItemRepository) {
        this.workReportRepository = workReportRepository;
        this.userService = userService;
        this.workOrderItemRepository = workOrderItemRepository;
    }

    public Page<WorkReport> findByWorkOrderId(Long workOrderId, Pageable pageable) {
        return workReportRepository.findByWorkOrderIdAndDeletedFalse(workOrderId, pageable);
    }

    public WorkReport findById(Long id) {
        return workReportRepository.findById(id).orElseThrow(() -> new RuntimeException("报工记录不存在：" + id));
    }

    @Transactional
    public WorkReport create(WorkReportCreateRequest workReportCreateRequest, Long reportedBy) {
        // 校验：若绑定了产品明细，检查本次报工量不超过剩余量
        Long itemId = workReportCreateRequest.getWorkOrderItemId();
        if (itemId != null) {
            WorkOrderItem item = workOrderItemRepository.findById(itemId)
                    .orElseThrow(() -> new RuntimeException("工单产品明细不存在：" + itemId));
            int remaining = item.getPlannedQty() - item.getCompletedQty();
            if (remaining <= 0) {
                throw new RuntimeException("产品【" + item.getProduct().getName() + "】已完成，无需再报工");
            }
            if (workReportCreateRequest.getReportedQty() > remaining) {
                throw new RuntimeException("报工数量 " + workReportCreateRequest.getReportedQty()
                        + " 超出剩余计划量 " + remaining);
            }
        }
        WorkReport workReport = new WorkReport();
        User user = userService.findById(reportedBy);
        workReport.setWorkOrderId(workReportCreateRequest.getWorkOrderId());
        workReport.setReportedQty(workReportCreateRequest.getReportedQty());
        workReport.setReportedBy(reportedBy);
        workReport.setReportedByName(user.getUsername());
        workReport.setReportedTime(System.currentTimeMillis());
        workReport.setStatus("DRAFT");
        workReport.setWorkOrderItemId(itemId);
        workReport.setProductName(workReportCreateRequest.getProductName());
        return workReportRepository.save(workReport);
    }

    /**
     * 工人提交报工申请（DRAFT → SUBMITTED）。
     * 只有记录创建人本人才能提交，且状态必须是 DRAFT。
     */
    @Transactional
    public WorkReport submit(Long id, Long reportedBy) {
        WorkReport workReport = this.findById(id);
        if (!reportedBy.equals(workReport.getReportedBy())) {
            throw new RuntimeException("只能提交自己的报工记录");
        }
        if (!workReport.getStatus().equals("DRAFT")) {
            throw new RuntimeException("只有草稿状态的记录才能提交，当前状态：" + workReport.getStatus());
        }
        workReport.setStatus("SUBMITTED");
        return workReportRepository.save(workReport);
    }

    /** 查询当前用户自己的报工记录（工人视角）。 */
    public Page<WorkReport> findMyReports(Long reportedBy, Pageable pageable) {
       return  workReportRepository.findByReportedByAndDeletedFalse(reportedBy, pageable);
    }

    /** 查询待审核列表（质检视角，状态为 SUBMITTED）。 */
    public Page<WorkReport> findPendingReview(Pageable pageable) {
        return workReportRepository.findByStatusAndDeletedFalse("SUBMITTED", pageable);
    }

    /**
     * 审核通过（质检动作，SUBMITTED → APPROVED）。
     * 状态必须是 SUBMITTED 才能通过。
     */
    @Transactional
    public WorkReport approve(Long id, Long reviewedBy) {
        WorkReport workReport = this.findById(id);
        if (!workReport.getStatus().equals("SUBMITTED")) {
            throw new RuntimeException("只有已提交的记录才能审核，当前状态：" + workReport.getStatus());
        }
        workReport.setStatus("APPROVED");
        workReport.setReviewedBy(reviewedBy);
        workReport.setReviewedByName(userService.findById(reviewedBy).getUsername());
        workReport.setReviewedTime(System.currentTimeMillis());

        // 审核通过 → 累加对应产品明细的已完成数量
        if (workReport.getWorkOrderItemId() != null) {
            workOrderItemRepository.findById(workReport.getWorkOrderItemId()).ifPresent(item -> {
                item.setCompletedQty(item.getCompletedQty() + workReport.getReportedQty());
                workOrderItemRepository.save(item);
            });
        }

        return workReportRepository.save(workReport);
    }

    /**
     * 审核驳回（质检动作，SUBMITTED → REJECTED）。
     * 状态必须是 SUBMITTED，且驳回原因不能为空。
     */
    @Transactional
    public WorkReport reject(Long id, Long reviewedBy, String rejectReason) {
        if (rejectReason == null || rejectReason.isBlank()) {
            throw new RuntimeException("驳回原因不能为空");
        }
        WorkReport workReport = this.findById(id);
        if (!workReport.getStatus().equals("SUBMITTED")) {
            throw new RuntimeException("只有已提交的记录才能审核，当前状态：" + workReport.getStatus());
        }
        workReport.setStatus("REJECTED");
        workReport.setDeleted(true);   // 软删除：驳回后不再出现在查询列表
        workReport.setReviewedBy(reviewedBy);
        workReport.setReviewedByName(userService.findById(reviewedBy).getUsername());
        workReport.setReviewedTime(System.currentTimeMillis());
        workReport.setRejectReason(rejectReason);
        return workReportRepository.save(workReport);
    }

    /**
     * 级联软删除：工单软删除时，将该工单下所有未删除的报工记录一并软删除。
     */
    @Transactional
    public void softDeleteByWorkOrderId(Long workOrderId) {
        List<WorkReport> workReports = workReportRepository.findByWorkOrderIdAndDeletedFalse(workOrderId);
        workReports.forEach(report -> report.setDeleted(true));
        workReportRepository.saveAll(workReports);
    }
}
