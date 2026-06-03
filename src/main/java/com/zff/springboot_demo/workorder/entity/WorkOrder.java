package com.zff.springboot_demo.workorder.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
/**
 * 工单实体，记录生产计划数量、完成数量和下发状态。
 */
@Entity
@Table(name = "work_orders")
public class WorkOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                    // 工单ID（自增主键）

    /**
     * 工单状态，通过状态机流转，不能随意跳跃：
     *   DRAFT（草稿）→ ISSUED（已下发）→ IN_PROGRESS（进行中）→ COMPLETED（已完成）
     *   ISSUED / IN_PROGRESS 可转 CANCELLED（已取消）
     */
    @Column(name = "status", nullable = false, length = 20)
    private String status = "DRAFT";

    @Column(name = "created_by", nullable = false)
    private Long createdBy;             // 创建人用户ID（关联 users 表的 id）

    @Column(name = "created_time", nullable = false)
    private Long createdTime;           // 创建时间戳（毫秒，System.currentTimeMillis()）

    @Column(name = "name", nullable = false, length = 100)
    private String name;                // 工单名称

    @Column(name = "created_by_name", length = 50)
    private String createdByName;       // 创建人用户名（创建时快照）

    /** 是否已软删除（删除时置为 true，不再出现在查询列表中） */
    @Column(name = "deleted", nullable = false)
    private Boolean deleted = false;

    /** 创建时间 */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /** 最后更新时间 */
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        if (createdAt == null) {
            createdAt = now;
        }
        if (updatedAt == null) {
            updatedAt = now;
        }
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @OneToMany(mappedBy = "workOrder", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("items")
    private List<WorkOrderItem> items = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<WorkOrderItem> getItems() {
        return items;
    }

    public void setItems(List<WorkOrderItem> items) {
        this.items = items;
    }
}
