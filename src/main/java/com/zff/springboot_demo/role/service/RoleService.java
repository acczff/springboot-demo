package com.zff.springboot_demo.role.service;

import com.zff.springboot_demo.permission.entity.Permission;
import com.zff.springboot_demo.permission.repository.PermissionRepository;
import com.zff.springboot_demo.role.entity.Role;
import com.zff.springboot_demo.role.repository.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 角色业务逻辑层，负责角色维护和权限绑定。
 */
@Service
public class RoleService {

    private final RoleRepository roleRepository;

    private final PermissionRepository permissionRepository;

    public RoleService(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    /**
     * 查询全部角色。
     */
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    /**
     * 创建角色，并校验角色名不能重复。
     */
    public Role createRole(Role role) {
        if (roleRepository.findByName(role.getName()) != null) {
            throw new RuntimeException("角色名 " + role.getName() + " 已存在");
        }
        return roleRepository.save(role);
    }

    /**
     * 查询指定角色拥有的权限列表。
     */
    public List<Permission> getRolePermissions(Long roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("角色不存在"));
        return role.getPermissions();
    }

    /**
     * 为角色重新分配权限，传入的权限 ID 会覆盖原有绑定。
     */
    @Transactional
    public Role assignPermissions(Long roleId, List<Long> permissionIds) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("角色不存在"));
        List<Permission> permissions = permissionRepository.findAllById(permissionIds);
        role.setPermissions(permissions);
        return roleRepository.save(role);
    }
}
