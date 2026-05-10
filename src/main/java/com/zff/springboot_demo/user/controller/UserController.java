package com.zff.springboot_demo.user.controller;

import com.zff.springboot_demo.Result;
import com.zff.springboot_demo.dto.PageResult;
import com.zff.springboot_demo.operationlog.LogOperation;
import com.zff.springboot_demo.operationlog.entity.OperationLog;
import com.zff.springboot_demo.operationlog.service.OperationLogService;
import com.zff.springboot_demo.role.entity.Role;
import com.zff.springboot_demo.user.entity.User;
import com.zff.springboot_demo.user.repository.UserRepository;
import com.zff.springboot_demo.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
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

    private final UserRepository userRepository;

    private final OperationLogService operationLogService;

    public UserController(
            UserService userService,
            UserRepository userRepository,
            OperationLogService operationLogService
    ) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.operationLogService = operationLogService;
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
        User user = userService.findById(id);
        if (user == null) {
            return Result.error(404,"用户不存在");
        }else {
            return Result.success("查询成功", user);
        }
    }

    /**
     * 查询用户角色
     * @param id 用户 ID
     * @return 角色列表
     */
    @GetMapping("/{id}/roles")
    public Result<List<Role>> getUserRoles(@PathVariable Long id) {
        List<Role> roles = userService.getUserRoles(id);
        if (roles == null) return Result.error(404, "用户不存在");
        return Result.success("查询成功", roles);
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
        User user = userService.assignRoles(id, roleIds);
        if (user == null) return Result.error(404, "用户不存在");
        return Result.success("角色绑定成功", user);
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
