package com.zff.springboot_demo.menu.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "menus")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "path", length = 100)
    private String path;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "sort")
    private Integer sort;

    @Column(name = "required_role", length = 50)
    private String requiredRole;

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
