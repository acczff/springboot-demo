package com.zff.springboot_demo.check.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * 盘点计划实体。
 * 状态流：PENDING（待开始）→ IN_PROGRESS（盘点中）→ PENDING_REVIEW（待审核）→ COMPLETED（已完成）
 */
@Entity
@Table(name = "inventory_check_plans")
public class InventoryCheckPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 计划状态 */
    @Column(name = "status", nullable = false, length = 20)
    private String status = "PENDING";

    /** 创建人（仓库主管ID） */
    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    /** 创建时间 */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /** 审核人（仓库主管ID，审核后才有值） */
    @Column(name = "reviewed_by")
    private Long reviewedBy;

    /** 审核时间 */
    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;

    /** 审核备注 */
    @Column(name = "review_note", length = 500)
    private String reviewNote;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Long getReviewedBy() { return reviewedBy; }
    public void setReviewedBy(Long reviewedBy) { this.reviewedBy = reviewedBy; }

    public LocalDateTime getReviewedAt() { return reviewedAt; }
    public void setReviewedAt(LocalDateTime reviewedAt) { this.reviewedAt = reviewedAt; }

    public String getReviewNote() { return reviewNote; }
    public void setReviewNote(String reviewNote) { this.reviewNote = reviewNote; }
}
