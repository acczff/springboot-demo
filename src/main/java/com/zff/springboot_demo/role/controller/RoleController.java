package com.zff.springboot_demo.role.controller;

import com.zff.springboot_demo.Result;
import com.zff.springboot_demo.operationlog.LogOperation;
import com.zff.springboot_demo.operationlog.entity.OperationLog;
import com.zff.springboot_demo.operationlog.service.OperationLogService;
import com.zff.springboot_demo.permission.entity.Permission;
import com.zff.springboot_demo.role.entity.Role;
import com.zff.springboot_demo.role.service.RoleService;
import com.zff.springboot_demo.user.entity.User;
import com.zff.springboot_demo.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;


import java.util.List;

/**
 * 角色管理接口
 */
@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }
    /**
     * 查询所有角色
     * @return 角色列表
     */
    @GetMapping
    public Result<List<Role>> findAll() {
        List<Role> roles = roleService.findAll();
        return Result.success("role findAll success", roles);
    }

    /**
     * 创建角色
     * @param role 角色信息
     * @return 创建后的角色
     */
    @LogOperation("新增角色")
    @PostMapping
    public Result<Role> createRole(@RequestBody Role role) {
        Role created = roleService.createRole(role);
        return Result.success("role createRole success", created);
    }

    /**
     * 查询角色权限
     * @param id 角色 ID
     * @return 权限列表
     */
    @GetMapping("/{id}/permissions")
    public Result<List<Permission>> getRolePermissions(@PathVariable Long id) {
        List<Permission> permissions = roleService.getRolePermissions(id);
        return Result.success("success", permissions);
    }

    /**
     * 为角色分配权限
     * @param id 角色 ID
     * @param permissionIds 权限 ID 列表
     * @return 更新后的角色
     */
    @LogOperation("分配权限")
    @PutMapping("/{id}/permissions")
    public Result<Role> assignPermissions(@PathVariable Long id, @RequestBody List<Long> permissionIds) {
        Role role = roleService.assignPermissions(id, permissionIds);
        return Result.success("分配成功", role);
    }
}
