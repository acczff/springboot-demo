package com.zff.springboot_demo.stockin.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * 入库单实体。
 * 状态流：PENDING_RECEIVE（待收货）→ RECEIVED（已验收）→ SHELVED（已上架）
 */
@Entity
@Table(name = "stock_in_orders")
public class StockInOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 产品ID */
    @Column(name = "product_id", nullable = false)
    private Long productId;

    /** 入库数量 */
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    /** 单据状态 */
    @Column(name = "status", nullable = false, length = 20)
    private String status = "PENDING_RECEIVE";

    /** 库管员ID */
    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    /** 创建时间 */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /** 验收时间 */
    @Column(name = "received_at")
    private LocalDateTime receivedAt;

    /** 上架时间 */
    @Column(name = "shelved_at")
    private LocalDateTime shelvedAt;

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

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getReceivedAt() { return receivedAt; }
    public void setReceivedAt(LocalDateTime receivedAt) { this.receivedAt = receivedAt; }

    public LocalDateTime getShelvedAt() { return shelvedAt; }
    public void setShelvedAt(LocalDateTime shelvedAt) { this.shelvedAt = shelvedAt; }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
