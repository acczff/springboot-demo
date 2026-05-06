package com.zff.springboot_demo.permission.service;

import com.zff.springboot_demo.permission.entity.Permission;
import com.zff.springboot_demo.permission.repository.PermissionRepository;
import com.zff.springboot_demo.role.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;

    public List<Permission> findAll() {
        return permissionRepository.findAll();
    }

    public Permission createPermission(Permission permission) {
        if (permissionRepository.findByCode(permission.getCode()) != null) {
            throw new RuntimeException("权限码 " + permission.getCode() + " 已存在");
        }
        return permissionRepository.save(permission);
    }
}
