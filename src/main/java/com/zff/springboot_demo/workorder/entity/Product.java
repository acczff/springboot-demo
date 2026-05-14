package com.zff.springboot_demo.workorder.entity;

import jakarta.persistence.*;

/**
 * 产品实体，作为工单关联的生产对象。
 */
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;           // 产品ID（自增主键）

    @Column(length = 50, nullable = false, unique = true)
    private String code;       // 产品编码，全局唯一，如 "P001"

    @Column(length = 100, nullable = false)
    private String name;       // 产品名称，如 "螺丝M5"

    @Column(length = 20, nullable = false)
    private String unit;       // 计量单位，如 "个"、"箱"、"kg"

    @Column(name = "created_time", nullable = false)
    private Long createdTime;  // 创建时间戳（毫秒）

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }
}
