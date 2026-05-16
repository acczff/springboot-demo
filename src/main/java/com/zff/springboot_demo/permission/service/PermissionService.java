package com.zff.springboot_demo.permission.service;

import com.zff.springboot_demo.permission.entity.Permission;
import com.zff.springboot_demo.permission.repository.PermissionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 权限业务逻辑层，负责权限查询和创建校验。
 */
@Service
public class PermissionService {


    private final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    /**
     * 查询全部权限。
     */
    public List<Permission> findAll() {
        return permissionRepository.findAll();
    }

    /**
     * 创建权限，并校验权限码不能重复。
     */
    @Transactional
    public Permission createPermission(Permission permission) {
        if (permissionRepository.findByCode(permission.getCode()) != null) {
            throw new IllegalArgumentException("权限码 " + permission.getCode() + " 已存在");
        }
        return permissionRepository.save(permission);
    }
}
