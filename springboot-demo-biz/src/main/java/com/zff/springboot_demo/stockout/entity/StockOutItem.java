package com.zff.springboot_demo.stockout.entity;

import jakarta.persistence.*;

/**
 * 出库明细实体。
 * 每条记录对应出库单中一个产品的计划出库数量。
 */
@Entity
@Table(name = "stock_out_items")
public class StockOutItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "planned_quantity", nullable = false)
    private Integer plannedQuantity;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public Integer getPlannedQuantity() { return plannedQuantity; }
    public void setPlannedQuantity(Integer plannedQuantity) { this.plannedQuantity = plannedQuantity; }
}
