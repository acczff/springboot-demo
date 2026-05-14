package com.zff.springboot_demo.menu.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 菜单实体，对应前端路由菜单配置。
 */
@Entity
@Table(name = "menus")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;           // 菜单显示名称，如 "用户管理"

    @Column(name = "path", length = 100)
    private String path;           // 前端路由路径，如 "/users"

    @Column(name = "parent_id")
    private Long parentId;         // 父菜单ID，为 null 表示一级菜单

    @Column(name = "sort")
    private Integer sort;          // 排序权重，数字越小越靠前

    @Column(name = "required_role", length = 50)
    private String requiredRole;   // 可见所需角色名，如 "ADMIN"；为 null 表示所有人可见

    /**
     * 子菜单列表，仅用于接口返回，不持久化到数据库。
     */
    @Transient
    private List<Menu> children = new ArrayList<>();

    public Menu() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public List<Menu> getChildren() {
        return children;
    }

    public void setChildren(List<Menu> children) {
        this.children = children;
    }

    public String getRequiredRole() { return requiredRole; }

    public void setRequiredRole(String requiredRole) { this.requiredRole = requiredRole; }
}
