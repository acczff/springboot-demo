package com.zff.springboot_demo.permission.repository;

import com.zff.springboot_demo.permission.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission,Long> {

    Permission findByCode(String code);

}
