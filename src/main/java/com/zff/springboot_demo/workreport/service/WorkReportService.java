package com.zff.springboot_demo.workreport.service;

import com.zff.springboot_demo.user.entity.User;
import com.zff.springboot_demo.user.service.UserService;
import com.zff.springboot_demo.workreport.dto.WorkReportCreateRequest;
import com.zff.springboot_demo.workreport.entity.WorkReport;
import com.zff.springboot_demo.workreport.repository.WorkReportRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WorkReportService {

    private final WorkReportRepository workReportRepository;
    private final UserService  userService;

    public WorkReportService(WorkReportRepository workReportRepository, UserService userService) {
        this.workReportRepository = workReportRepository;
        this.userService = userService;
    }

    public Page<WorkReport> findByWorkOrderId(Long workOrderId, Pageable pageable) {
        return workReportRepository.findByWorkOrderIdAndDeletedFalse(workOrderId, pageable);
    }

    public WorkReport findById(Long id) {
        return workReportRepository.findById(id).orElseThrow(() -> new RuntimeException("报工记录不存在：" + id));
    }

    @Transactional
    public WorkReport create(WorkReportCreateRequest workReportCreateRequest,Long reportedBy) {
        WorkReport workReport = new WorkReport();
        User user = userService.findById(reportedBy);
        workReport.setWorkOrderId(workReportCreateRequest.getWorkOrderId());
        workReport.setReportedQty(workReportCreateRequest.getReportedQty());
        workReport.setReportedBy(reportedBy);
        workReport.setReportedByName(user.getUsername());
        workReport.setReportedTime(System.currentTimeMillis());
        workReport.setStatus("DRAFT");
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
}
