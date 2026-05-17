package com.zff.springboot_demo.workreport.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "work_reports")
public class WorkReport {

    /** 报工记录主键，自增 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 所属工单 ID（关联 work_orders 表） */
    @Column(name = "work_order_id", nullable = false)
    private Long workOrderId;

    /** 本次报工数量 */
    @Column(name = "reported_qty", nullable = false)
    private Integer reportedQty;

    /** 报工人 ID（从 token 自动注入，不允许前端传） */
    @Column(name = "reported_by", nullable = false)
    private Long reportedBy;

    /** 报工人用户名快照（创建时从用户表取，后续改名不影响历史记录） */
    @Column(name = "reported_by_name", length = 50)
    private String reportedByName;

    /** 报工时间，毫秒时间戳 */
    @Column(name = "reported_time", nullable = false)
    private Long reportedTime;

    /**
     * 报工状态流转：
     * DRAFT（草稿）→ SUBMITTED（已提交审核）→ APPROVED（已通过）
     *                                       ↘ REJECTED（已驳回）
     */
    @Column(name = "status", nullable = false, length = 20)
    private String status = "DRAFT";

    /** 审核人 ID（审核后写入） */
    @Column(name = "reviewed_by")
    private Long reviewedBy;

    /** 审核人用户名快照（审核后写入） */
    @Column(name = "reviewed_by_name", length = 50)
    private String reviewedByName;

    /** 审核时间，毫秒时间戳（审核后写入） */
    @Column(name = "reviewed_time")
    private Long reviewedTime;

    /** 驳回原因（仅状态为 REJECTED 时有值） */
    @Column(name = "reject_reason", length = 200)
    private String rejectReason;

    /** 是否已软删除（驳回时置为 true，不再出现在查询列表中） */
    @Column(name = "deleted", nullable = false)
    private Boolean deleted = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(Long workOrderId) {
        this.workOrderId = workOrderId;
    }

    public Integer getReportedQty() {
        return reportedQty;
    }

    public void setReportedQty(Integer reportedQty) {
        this.reportedQty = reportedQty;
    }

    public Long getReportedBy() {
        return reportedBy;
    }

    public void setReportedBy(Long reportedBy) {
        this.reportedBy = reportedBy;
    }

    public String getReportedByName() {
        return reportedByName;
    }

    public void setReportedByName(String reportedByName) {
        this.reportedByName = reportedByName;
    }

    public Long getReportedTime() {
        return reportedTime;
    }

    public void setReportedTime(Long reportedTime) {
        this.reportedTime = reportedTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getReviewedBy() {
        return reviewedBy;
    }

    public void setReviewedBy(Long reviewedBy) {
        this.reviewedBy = reviewedBy;
    }

    public String getReviewedByName() {
        return reviewedByName;
    }

    public void setReviewedByName(String reviewedByName) {
        this.reviewedByName = reviewedByName;
    }

    public Long getReviewedTime() {
        return reviewedTime;
    }

    public void setReviewedTime(Long reviewedTime) {
        this.reviewedTime = reviewedTime;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
