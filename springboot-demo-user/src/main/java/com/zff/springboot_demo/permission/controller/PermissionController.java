package com.zff.springboot_demo.permission.controller;

import com.zff.springboot_demo.Result;
import com.zff.springboot_demo.permission.entity.Permission;
import com.zff.springboot_demo.permission.service.PermissionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 权限管理接口
 */
@RestController
@RequestMapping("/api/permissions")
public class PermissionController {

    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    /**
     * 查询所有权限
     * @return 权限列表
     */
    @GetMapping
    public Result<List<Permission>> findAll() {
        List<Permission> permissions = permissionService.findAll();
        return Result.success("permissions findAll success", permissions);
    }

    /**
     * 创建权限
     * @param permission 权限信息
     * @return 创建后的权限
     */
    @PostMapping
    public Result<Permission> createPermission(@RequestBody Permission permission) {
        Permission created = permissionService.createPermission(permission);
        return Result.success("permission create Success", created);
    }
}
