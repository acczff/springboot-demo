package com.zff.springboot_demo.role.repository;


import com.zff.springboot_demo.role.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 角色数据访问层，继承 JpaRepository 提供基础 CRUD 能力。
 */
@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {

    /**
     * 根据角色名称查询角色。
     */
    Role findByName(String name);

}
