package com.zff.springboot_demo.inspection.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * 质检单实体。
 * 状态流：PENDING（待检）→ INSPECTING（检验中）→ PASS（合格）/ FAIL（不合格）
 */
@Entity
@Table(name = "inspection_orders")
public class InspectionOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 关联工单 ID */
    @Column(name = "work_order_id", nullable = false)
    private Long workOrderId;

    /** 质检状态：PENDING / INSPECTING / PASS / FAIL */
    @Column(name = "status", nullable = false, length = 20)
    private String status = "PENDING";

    /** 检验人 ID（开始检验时写入） */
    @Column(name = "inspector_id")
    private Long inspectorId;

    /** 检验人姓名快照（开始检验时写入） */
    @Column(name = "inspector_name", length = 50)
    private String inspectorName;

    /** 不合格原因（FAIL 时填写） */
    @Column(name = "fail_reason", length = 500)
    private String failReason;

    /** 检验完成时间（PASS / FAIL 时写入） */
    @Column(name = "inspected_at")
    private LocalDateTime inspectedAt;

    /** 创建人 ID（从 token 自动注入） */
    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    /** 创建人姓名快照 */
    @Column(name = "created_by_name", nullable = false, length = 50)
    private String createdByName;

    /** 创建时间 */
    @Column(name = "created_time", nullable = false)
    private LocalDateTime createdTime;

    // -------- getter / setter --------

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getWorkOrderId() { return workOrderId; }
    public void setWorkOrderId(Long workOrderId) { this.workOrderId = workOrderId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Long getInspectorId() { return inspectorId; }
    public void setInspectorId(Long inspectorId) { this.inspectorId = inspectorId; }

    public String getInspectorName() { return inspectorName; }
    public void setInspectorName(String inspectorName) { this.inspectorName = inspectorName; }

    public String getFailReason() { return failReason; }
    public void setFailReason(String failReason) { this.failReason = failReason; }

    public LocalDateTime getInspectedAt() { return inspectedAt; }
    public void setInspectedAt(LocalDateTime inspectedAt) { this.inspectedAt = inspectedAt; }

    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }

    public String getCreatedByName() { return createdByName; }
    public void setCreatedByName(String createdByName) { this.createdByName = createdByName; }

    public LocalDateTime getCreatedTime() { return createdTime; }
    public void setCreatedTime(LocalDateTime createdTime) { this.createdTime = createdTime; }
}
