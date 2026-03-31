package com.zff.springboot_demo.user.repository;

import com.zff.springboot_demo.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户数据访问层
 * 暂时使用内存存储（Map），后续会替换为真实数据库
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    /**
     * 根据用户名查找用户
     * @param username 用户名
     * @return 用户对象
     */
    User findByUsername(String username);

    /**
     * 根据邮箱查找用户
     * @param email 邮箱
     * @return 用户对象
     */
    User findByEmail(String email);

}
