package com.zff.springboot_demo.workorder.entity;

import jakarta.persistence.*;

/**
 * 工单实体，记录生产计划数量、完成数量和下发状态。
 */
@Entity
@Table(name = "work_orders")
public class WorkOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                    // 工单ID（自增主键）

    @Column(nullable = false)
    private int plannedQty;             // 计划生产数量

    @Column(nullable = false)
    private int completedQty = 0;       // 已完成数量（默认0，随进度更新）

    /**
     * 工单状态，通过状态机流转，不能随意跳跃：
     *   DRAFT（草稿）→ ISSUED（已下发）→ IN_PROGRESS（进行中）→ COMPLETED（已完成）
     *   ISSUED / IN_PROGRESS 可转 CANCELLED（已取消）
     */
    @Column(nullable = false, length = 20)
    private String status = "DRAFT";

    @Column(nullable = false)
    private Long createdBy;             // 创建人用户ID（关联 users 表的 id）

    @Column(nullable = false)
    private Long createdTime;           // 创建时间戳（毫秒，System.currentTimeMillis()）

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;            // 关联产品，多个工单可指向同一产品（多对一）

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getPlannedQty() {
        return plannedQty;
    }

    public void setPlannedQty(int plannedQty) {
        this.plannedQty = plannedQty;
    }

    public int getCompletedQty() {
        return completedQty;
    }

    public void setCompletedQty(int completedQty) {
        this.completedQty = completedQty;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
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
}
