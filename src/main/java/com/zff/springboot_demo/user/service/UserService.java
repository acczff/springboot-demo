package com.zff.springboot_demo.user.service;

import com.zff.springboot_demo.user.entity.User;
import com.zff.springboot_demo.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户业务逻辑层
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * 根据 ID 查找用户
     * @param id 用户 ID
     * @return 用户对象
     */
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    /**
     * 根据 username 查找用户
     * @param username 用户名
     * @return 用户对象
     */
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * 查找所有用户
     * @return 用户列表
     */
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }


    public Page<User> findAll(String keyword, Pageable pageable) {
        if (keyword == null || keyword.isBlank()) {
            return userRepository.findAll(pageable);  // 没有关键词，查全部
        }
        return userRepository.findByUsernameContainingOrEmailContaining(
                keyword, keyword, pageable
        );
    }
    /**
     * 创建用户
     * @param user 用户对象
     * @return 创建后的用户对象
     */
    public User createUser(User user) {
        return userRepository.save(user);
    }

    /**
     * 更新用户
     * @param id 用户 ID
     * @param user 用户对象
     * @return 更新后的用户对象，不存在返回 null
     */
    public User updateUser(Long id, User user) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setUsername(user.getUsername());
                    existingUser.setEmail(user.getEmail());
                    if (user.getPassword() != null && !user.getPassword().isBlank()) {
                        existingUser.setPassword(user.getPassword());
                    }
                    return userRepository.save(existingUser);
                })
                .orElse(null);
    }

    /**
     * 删除用户
     * @param id 用户 ID
     * @return 是否删除成功
     */

    public boolean deleteUser(Long id) {
        if(!userRepository.existsById(id)) {
            return false;
        }
        userRepository.deleteById(id);
        return true;
    }
}
