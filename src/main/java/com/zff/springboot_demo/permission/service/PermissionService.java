package com.zff.springboot_demo.permission.service;

import com.zff.springboot_demo.permission.entity.Permission;
import com.zff.springboot_demo.permission.repository.PermissionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionService {


    private final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public List<Permission> findAll() {
        return permissionRepository.findAll();
    }

    public Permission createPermission(Permission permission) {
        if (permissionRepository.findByCode(permission.getCode()) != null) {
            throw new IllegalArgumentException("权限码 " + permission.getCode() + " 已存在");
        }
        return permissionRepository.save(permission);
    }
}
