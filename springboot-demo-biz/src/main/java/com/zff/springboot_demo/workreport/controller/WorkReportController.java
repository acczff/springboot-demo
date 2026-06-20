package com.zff.springboot_demo.workreport.controller;

import com.zff.springboot_demo.Result;
import com.zff.springboot_demo.dto.PageResult;
import com.zff.springboot_demo.workreport.dto.WorkReportCreateRequest;
import com.zff.springboot_demo.workreport.dto.WorkReportReviewRequest;
import com.zff.springboot_demo.workreport.entity.WorkReport;
import com.zff.springboot_demo.workreport.service.WorkReportService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/work-reports")
/**
 * 报工记录接口。
 */
public class WorkReportController {

    private final WorkReportService workReportService;

    public WorkReportController(WorkReportService workReportService) {
        this.workReportService = workReportService;
    }

    /**
     * 按报工记录 ID 查询详情。
     * @param id 报工记录主键
     * @return 报工记录详情
     */
    @GetMapping("/{id}")
    public Result<WorkReport> findById(@PathVariable Long id) {
        return Result.success("查询成功", workReportService.findById(id));
    }

    /**
     * 按工单 ID 分页查询报工记录（主管视角）。
     * @param pageNum 页码（从 1 开始）
     * @param pageSize 每页条数
     * @param workOrderId 工单 ID
     * @return 报工记录分页结果
     */
    @GetMapping
    public Result<PageResult<WorkReport>> listByWorkOrderId(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam Long workOrderId) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page<WorkReport> page =   workReportService.findByWorkOrderId(workOrderId,pageable);
        PageResult<WorkReport> pageResult = new PageResult<>(page.getContent(), page.getTotalElements());
        return Result.success("查询成功",pageResult);
    }

    /**
     * 创建报工记录。
     * @param request 报工请求参数
     * @param httpRequest HTTP 请求对象（用于读取当前登录用户）
     * @return 创建后的报工记录
     */
    @PostMapping
    public Result<WorkReport> create(
            @RequestBody @Valid WorkReportCreateRequest request,
            HttpServletRequest httpRequest) {
        Long reportedBy = (Long) httpRequest.getAttribute("currentUserId");
        return Result.success("创建成功", workReportService.create(request, reportedBy));
    }

    // ==================== 工人视角 ====================

    /**
     * 查询我的报工记录（工人视角，只能看自己提交的）。
     * reportedBy 从 token 自动取，不接受前端传参。
     */
    @GetMapping("/me")
    public Result<PageResult<WorkReport>> findMyReports(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            HttpServletRequest httpRequest) {
        Long reportedBy = (Long) httpRequest.getAttribute("currentUserId");
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page<WorkReport> page = workReportService.findMyReports(reportedBy, pageable);
        return Result.success("查询成功", new PageResult<>(page.getContent(), page.getTotalElements()));
    }

    /**
     * 工人提交报工申请（DRAFT → SUBMITTED）。
     * 只有本人才能提交自己创建的记录。
     * @param id 报工记录 ID
     */
    @PutMapping("/{id}/submit")
    public Result<WorkReport> submit(@PathVariable Long id, HttpServletRequest httpRequest) {
        Long reportedBy = (Long) httpRequest.getAttribute("currentUserId");
        return Result.success("提交成功", workReportService.submit(id, reportedBy));
    }

    // ==================== 质检视角 ====================

    /**
     * 查询待审核列表（质检视角，状态为 SUBMITTED 的所有记录）。
     */
    @GetMapping("/pending-review")
    public Result<PageResult<WorkReport>> findPendingReview(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page<WorkReport> page = workReportService.findPendingReview(pageable);
        return Result.success("查询成功", new PageResult<>(page.getContent(), page.getTotalElements()));
    }

    /**
     * 审核通过（质检动作，SUBMITTED → APPROVED）。
     * @param id 报工记录 ID
     */
    @PutMapping("/{id}/approve")
    public Result<WorkReport> approve(@PathVariable Long id, HttpServletRequest httpRequest) {
        Long reviewedBy = (Long) httpRequest.getAttribute("currentUserId");
        return Result.success("审核通过", workReportService.approve(id, reviewedBy));
    }

    /**
     * 审核驳回（质检动作，SUBMITTED → REJECTED）。
     * @param id 报工记录 ID
     * @param request 驳回请求（rejectReason 必填）
     */
    @PutMapping("/{id}/reject")
    public Result<WorkReport> reject(
            @PathVariable Long id,
            @RequestBody WorkReportReviewRequest request,
            HttpServletRequest httpRequest) {
        Long reviewedBy = (Long) httpRequest.getAttribute("currentUserId");
        return Result.success("已驳回", workReportService.reject(id, reviewedBy, request.getRejectReason()));
    }
}
