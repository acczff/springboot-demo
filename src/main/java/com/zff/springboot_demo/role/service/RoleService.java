package com.zff.springboot_demo.role.service;

import com.zff.springboot_demo.role.entity.Role;
import com.zff.springboot_demo.role.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

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
}
