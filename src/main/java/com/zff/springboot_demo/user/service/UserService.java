package com.zff.springboot_demo.user.service;

import com.zff.springboot_demo.user.entity.User;
import com.zff.springboot_demo.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<User> findAll() {
        return userRepository.findAll();
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
                    user.setId(id);
                    return userRepository.save(user);
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
