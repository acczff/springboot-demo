package com.zff.springboot_demo.workorder.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "work_order_items")
public class WorkOrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "work_order_id", nullable = false)
    @JsonIgnoreProperties("items")
    private WorkOrder workOrder;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "planned_qty", nullable = false)
    private int plannedQty;

    @Column(name = "completed_qty", nullable = false)
    private int completedQty;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public WorkOrder getWorkOrder() {
        return workOrder;
    }

    public void setWorkOrder(WorkOrder workOrder) {
        this.workOrder = workOrder;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
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
}
