package com.zff.springboot_demo.role.controller;

import com.zff.springboot_demo.Result;
import com.zff.springboot_demo.permission.entity.Permission;
import com.zff.springboot_demo.role.entity.Role;
import com.zff.springboot_demo.role.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    RoleService roleService;

    @GetMapping
    public Result<List<Role>> findAll() {
        List<Role> roles = roleService.findAll();
        return Result.success("role findAll success", roles);
    }

    @PostMapping
    public Result<Role> createRole(@RequestBody Role role) {
        try {
            Role created = roleService.createRole(role);
            return Result.success("role createRole success", created);
        } catch (RuntimeException e) {
            return Result.error(400, e.getMessage());
        }
    }

    // 查询角色的权限列表
    @GetMapping("/{id}/permissions")
    public Result<List<Permission>> getRolePermissions(@PathVariable Long id) {
        List<Permission> permissions = roleService.getRolePermissions(id);
        return Result.success("success", permissions);
    }

    // 分配权限给角色
    @PutMapping("/{id}/permissions")
    public Result<Role> assignPermissions(@PathVariable Long id, @RequestBody List<Long> permissionIds) {
        Role role = roleService.assignPermissions(id, permissionIds);
        return Result.success("分配成功", role);
    }
}
