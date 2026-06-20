package com.zff.springboot_demo.permission.repository;

import com.zff.springboot_demo.permission.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 权限数据访问层，封装权限表查询。
 */
@Repository
public interface PermissionRepository extends JpaRepository<Permission,Long> {

    /**
     * 根据权限码查询权限。
     */
    Permission findByCode(String code);

}
