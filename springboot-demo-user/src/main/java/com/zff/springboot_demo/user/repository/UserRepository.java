package com.zff.springboot_demo.user.repository;

import com.zff.springboot_demo.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 用户数据访问层
 * 继承 JpaRepository，提供用户表的基础 CRUD 和派生查询。
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    /**
     * 根据用户名查找用户
     * @param username 用户名
     * @return 用户对象
     */
    User findByUsername(String username);

    @EntityGraph(attributePaths = {"roles", "roles.permissions"})
    User findWithRolesByUsername(String username);

    @EntityGraph(attributePaths = {"roles", "roles.permissions"})
    Optional<User> findWithRolesById(Long id);

    /**
     * 根据邮箱查找用户
     * @param email 邮箱
     * @return 用户对象
     */
    User findByEmail(String email);

    /**
     * 根据用户名或邮箱模糊分页查询用户。
     */
    Page<User> findByUsernameContainingOrEmailContaining(String username, String email, Pageable pageable);

//    // 继承 JpaRepository 后自动获得的方法
//    findById(Long id)        // 根据 ID 查询
//    findAll()                // 查询所有
//    save(User user)          // 保存（新增或更新）
//    deleteById(Long id)      // 根据 ID 删除
//    existsById(Long id)      // 判断是否存在
//    count()                  // 统计总数

}
