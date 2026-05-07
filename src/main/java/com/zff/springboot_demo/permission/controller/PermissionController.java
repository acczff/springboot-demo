package com.zff.springboot_demo.permission.controller;

import com.zff.springboot_demo.Result;
import com.zff.springboot_demo.permission.entity.Permission;
import com.zff.springboot_demo.permission.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permissions")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @GetMapping
    public Result<List<Permission>> findAll() {
        List<Permission> permissions = permissionService.findAll();
        return Result.success("permissions findAll success", permissions);
    }

    @PostMapping
    public Result<Permission> createPermission(@RequestBody Permission permission) {
        try {
            Permission created = permissionService.createPermission(permission);
            return Result.success("permission create Success", created);
        } catch (RuntimeException e) {
            return Result.error(400, e.getMessage());
        }
    }
}
