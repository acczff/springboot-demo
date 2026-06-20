package com.zff.springboot_demo.user.controller;

import com.zff.springboot_demo.Result;
import com.zff.springboot_demo.dto.PageResult;
import com.zff.springboot_demo.operationlog.LogOperation;
import com.zff.springboot_demo.role.entity.Role;
import com.zff.springboot_demo.user.entity.User;
import com.zff.springboot_demo.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户管理控制器
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 分页查询用户
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @param keyword 搜索关键字
     * @return 用户分页数据
     */
    @GetMapping
    public Result<PageResult<User>> findAll(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String keyword
    ) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page<User> page = userService.findAll(keyword, pageable);  // ← 传 keyword
        PageResult<User> pageResult = new PageResult<>(page.getContent(), page.getTotalElements());
        return Result.success("查询成功", pageResult);
    }

    /**
     * 根据 ID 获取用户
     * @param id 用户 ID
     * @return 用户对象
     */
    @GetMapping("/{id}")
    public Result<User> getUserById(@PathVariable Long id) {
        return Result.success("查询成功", userService.findById(id));
    }

    /**
     * 查询用户角色
     * @param id 用户 ID
     * @return 角色列表
     */
    @GetMapping("/{id}/roles")
    public Result<List<Role>> getUserRoles(@PathVariable Long id) {
        return Result.success("查询成功", userService.getUserRoles(id));
    }

    /**
     * 判断用户是否为管理员
     * @param id 用户 ID
     * @return 是否为管理员
     */
    @GetMapping("/{id}/isAdmin")
    public Result<Boolean> isAdmin(@PathVariable Long id) {
        return Result.success("查询成功", userService.isAdmin(id));
    }

    /**
     * 绑定用户角色
     * @param id 用户 ID
     * @param roleIds 角色 ID 列表
     * @return 更新后的用户
     */
    @LogOperation("绑定角色")
    @PutMapping("/{id}/roles")
    public Result<User> assignRoles(@PathVariable Long id, @RequestBody List<Long> roleIds) {
        return Result.success("角色绑定成功", userService.assignRoles(id, roleIds));
    }

    /**
     * 创建用户
     * @param user 用户对象
     * @return 创建后的用户对象
     */
    @PostMapping
    public Result<User> createUser(@RequestBody @Valid User user) {
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
        return Result.success("用户更新成功", userService.updateUser(id, user));
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
