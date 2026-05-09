package com.zff.springboot_demo.role.service;

import com.zff.springboot_demo.permission.entity.Permission;
import com.zff.springboot_demo.permission.repository.PermissionRepository;
import com.zff.springboot_demo.role.entity.Role;
import com.zff.springboot_demo.role.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    private final PermissionRepository permissionRepository;

    public RoleService(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    // 查所有角色
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    // 新增角色
    public Role createRole(Role role) {
        if (roleRepository.findByName(role.getName()) != null) {
            throw new RuntimeException("角色名 " + role.getName() + " 已存在");
        }
        return roleRepository.save(role);
    }

    // 查询角色的权限列表
    public List<Permission> getRolePermissions(Long roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("角色不存在"));
        return role.getPermissions();
    }

    // 分配权限给角色
    public Role assignPermissions(Long roleId, List<Long> permissionIds) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("角色不存在"));
        List<Permission> permissions = permissionRepository.findAllById(permissionIds);
        role.setPermissions(permissions);
        return roleRepository.save(role);
    }
}
