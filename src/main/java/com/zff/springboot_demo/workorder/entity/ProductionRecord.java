package com.zff.springboot_demo.workorder.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "production_records")
public class ProductionRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "work_order_item_id", nullable = false)
    private WorkOrderItem workOrderItem;

    @Column(name = "operator_id", nullable = false)
    private Long operatorId;

    @Column(name = "qty", nullable = false)
    private Integer qty;

    @Column(name = "completed_time", nullable = false)
    private Long completedTime;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public WorkOrderItem getWorkOrderItem() {
        return workOrderItem;
    }

    public void setWorkOrderItem(WorkOrderItem workOrderItem) {
        this.workOrderItem = workOrderItem;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Long getCompletedTime() {
        return completedTime;
    }

    public void setCompletedTime(Long completedTime) {
        this.completedTime = completedTime;
    }
}
