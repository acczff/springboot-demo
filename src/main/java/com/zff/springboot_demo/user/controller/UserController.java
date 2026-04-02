package com.zff.springboot_demo.user.controller;

import com.zff.springboot_demo.Result;
import com.zff.springboot_demo.user.entity.User;
import com.zff.springboot_demo.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户管理控制器
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserService userService;

    /**
     * 获取所有用户
     * @return 用户列表
     */
    @GetMapping
    public Result<List<User>> findAll() {
        List<User> users = userService.findAll();
        return Result.success("查询成功", users);
    }

    /**
     * 根据 ID 获取用户
     * @param id 用户 ID
     * @return 用户对象
     */
    @GetMapping("/{id}")
    public Result<User> getUserById(@PathVariable Long id) {
        User user = userService.findById(id);
        if (user == null) {
            return Result.error(404,"用户不存在");
        }else {
            return Result.success("查询成功", user);
        }
    }

    /**
     * 创建用户
     * @param user 用户对象
     * @return 创建后的用户对象
     */
    @PostMapping
    public Result<User> createUser(@RequestBody User user) {
        user.setCreateTime(System.currentTimeMillis());
        User createUser = userService.createUser(user);
        return Result.success("用户创建成功", createUser);
    }

    /**
     * 更新用户
     * @param id 用户 ID
     * @param user 用户对象
     * @return 更新后的用户对象
     */
    @PutMapping("/{id}")
    public Result<User> updateUser(@PathVariable Long id,@RequestBody User user) {
        User updateUser =  userService.updateUser(id, user);
        if (updateUser == null) {
            return Result.error(404, "用户为空");
        }
        return Result.success("用户更新成功", updateUser);
    }

    /**
     * 删除用户
     * @param id 用户 ID
     * @return 删除结果
     */
//    @DeleteMapping("/{id}")
//    public Result<Void> deleteUser(@PathVariable Long id) {
//        boolean deleted =  userService.deleteUser(id);
//        if (!deleted) {
//            return Result.error(404,"用户不存在");
//        }
//        return Result.success("删除成功",null);
//    }
}
