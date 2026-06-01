package com.zff.springboot_demo.check.entity;

import jakarta.persistence.*;

/**
 * 盘点明细实体。一条记录对应一次盘点计划中的一个产品。
 * actual_quantity / difference 在盘点员录入前为 null。
 */
@Entity
@Table(name = "check_items")
public class CheckItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 所属盘点计划ID */
    @Column(name = "plan_id", nullable = false)
    private Long planId;

    /** 产品ID */
    @Column(name = "product_id", nullable = false)
    private Long productId;

    /** 账面数（计划创建时从 inventory 快照，全程不变） */
    @Column(name = "system_quantity", nullable = false)
    private Integer systemQuantity;

    /** 实盘数（盘点员填，未盘时为 null） */
    @Column(name = "actual_quantity")
    private Integer actualQuantity;

    /** 差异 = actual - system（提交时系统写入，未提交时为 null） */
    @Column(name = "difference")
    private Integer difference;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getPlanId() { return planId; }
    public void setPlanId(Long planId) { this.planId = planId; }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public Integer getSystemQuantity() { return systemQuantity; }
    public void setSystemQuantity(Integer systemQuantity) { this.systemQuantity = systemQuantity; }

    public Integer getActualQuantity() { return actualQuantity; }
    public void setActualQuantity(Integer actualQuantity) { this.actualQuantity = actualQuantity; }

    public Integer getDifference() { return difference; }
    public void setDifference(Integer difference) { this.difference = difference; }
}
