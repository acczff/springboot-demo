package com.zff.springboot_demo.user.repository;

import com.zff.springboot_demo.user.entity.User;
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
public class UserRepository {

    private final Map<Long, User> userStorage = new ConcurrentHashMap<>();
    private Long nextId = 1L;

    /**
     * 根据 ID 查找用户
     * @param id 用户 ID
     * @return 用户对象，不存在返回 null
     */
    public User findById(Long id) {
        return userStorage.get(id);
    }

    /**
     * 查找所有用户
     * @return 用户列表
     */
    public List<User> findAll() {
        return new ArrayList<>(userStorage.values());
    }

    /**
     * 保存用户（新增或更新）
     * @param user 用户对象
     * @return 保存后的用户对象（包含 ID）
     */
    public User save(User user) {
        if (user.getId() == null) {
            // 新增用户时 分配ID
            user.setId(nextId++);
        }
        userStorage.put(user.getId(), user);
        return user;
    }

    /**
     * 根据 ID 删除用户
     * @param id 用户 ID
     * @return 是否删除成功
     */
    public boolean delete(Long id) {
        return userStorage.remove(id) != null;
    }

}
