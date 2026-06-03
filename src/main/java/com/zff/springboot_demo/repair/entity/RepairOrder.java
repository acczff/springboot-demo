package com.zff.springboot_demo.repair.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * 设备维修工单实体。
 * 状态流：PENDING（待派单）→ ASSIGNED（已派单）→ IN_PROGRESS（维修中）
 *       → PENDING_CONFIRM（待确认）→ CLOSED（已关闭）
 */
@Entity
@Table(name = "repair_orders")
public class RepairOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 设备编号 */
    @Column(name = "equipment_code", nullable = false, length = 100)
    private String equipmentCode;

    /** 故障描述（操作工填） */
    @Column(name = "fault_description", nullable = false, columnDefinition = "TEXT")
    private String faultDescription;

    /** 工单状态 */
    @Column(name = "status", nullable = false, length = 20)
    private String status = "PENDING";

    /** 发现人ID（操作工） */
    @Column(name = "reporter_id", nullable = false)
    private Long reporterId;

    /** 维修主管ID */
    @Column(name = "supervisor_id")
    private Long supervisorId;

    /** 维修员ID */
    @Column(name = "repairer_id")
    private Long repairerId;

    /** 维修记录（维修员填） */
    @Column(name = "repair_result", columnDefinition = "TEXT")
    private String repairResult;

    /** 确认记录（操作工填） */
    @Column(name = "confirmation_opinion", columnDefinition = "TEXT")
    private String confirmationOpinion;

    /** 报修时间 */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /** 派单时间 */
    @Column(name = "assigned_at")
    private LocalDateTime assignedAt;

    /** 开始维修时间 */
    @Column(name = "started_at")
    private LocalDateTime startedAt;

    /** 维修完成时间 */
    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    /** 确认时间 */
    @Column(name = "confirmed_at")
    private LocalDateTime confirmedAt;

    /** 最后更新时间 */
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        if (createdAt == null) { createdAt = now; }
        if (updatedAt == null) { updatedAt = now; }
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // -------- getter / setter --------

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEquipmentCode() { return equipmentCode; }
    public void setEquipmentCode(String equipmentCode) { this.equipmentCode = equipmentCode; }

    public String getFaultDescription() { return faultDescription; }
    public void setFaultDescription(String faultDescription) { this.faultDescription = faultDescription; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Long getReporterId() { return reporterId; }
    public void setReporterId(Long reporterId) { this.reporterId = reporterId; }

    public Long getSupervisorId() { return supervisorId; }
    public void setSupervisorId(Long supervisorId) { this.supervisorId = supervisorId; }

    public Long getRepairerId() { return repairerId; }
    public void setRepairerId(Long repairerId) { this.repairerId = repairerId; }

    public String getRepairResult() { return repairResult; }
    public void setRepairResult(String repairResult) { this.repairResult = repairResult; }

    public String getConfirmationOpinion() { return confirmationOpinion; }
    public void setConfirmationOpinion(String confirmationOpinion) { this.confirmationOpinion = confirmationOpinion; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getAssignedAt() { return assignedAt; }
    public void setAssignedAt(LocalDateTime assignedAt) { this.assignedAt = assignedAt; }

    public LocalDateTime getStartedAt() { return startedAt; }
    public void setStartedAt(LocalDateTime startedAt) { this.startedAt = startedAt; }

    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }

    public LocalDateTime getConfirmedAt() { return confirmedAt; }
    public void setConfirmedAt(LocalDateTime confirmedAt) { this.confirmedAt = confirmedAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
