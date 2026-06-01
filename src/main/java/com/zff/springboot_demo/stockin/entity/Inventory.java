package com.zff.springboot_demo.stockin.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * 库存实体。一个产品一条记录（product_id 唯一索引）。
 * 上架时数量累加；本阶段不做出库扣减。
 */
@Entity
@Table(name = "inventory")
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 产品ID（唯一） */
    @Column(name = "product_id", nullable = false, unique = true)
    private Long productId;

    /** 库位 */
    @Column(name = "location", length = 100)
    private String location;

    /** 当前库存数量 */
    @Column(name = "quantity", nullable = false)
    private Integer quantity = 0;

    /** 首次入库时间 */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /** 最后更新时间 */
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
