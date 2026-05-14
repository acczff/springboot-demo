package com.zff.springboot_demo.permission.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * 权限实体，描述可授权的操作编码和说明。
 */
@Entity
@Table(name = "permissions")
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 权限码，全局唯一标识，如 "user:create"、"role:assign"，前端用来做按钮级权限控制
    @Column(nullable = false, length = 100)
    private String code;

    // 权限说明，描述该权限的含义，如 "创建用户"
    @Column(length = 200)
    private String description;

    public Permission() {}

    public Permission(Long id,String code, String description) {
        this.id = id;
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
