package com.zff.springboot_demo.menu.repository;

import com.zff.springboot_demo.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 菜单数据访问层，提供菜单表基础 CRUD 操作。
 */
@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {

}
