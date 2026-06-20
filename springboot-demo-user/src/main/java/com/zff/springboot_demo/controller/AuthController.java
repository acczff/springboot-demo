package com.zff.springboot_demo.controller;

import com.zff.springboot_demo.Result;
import com.zff.springboot_demo.dto.login.LoginRequest;
import com.zff.springboot_demo.dto.login.LoginResponse;
import com.zff.springboot_demo.exception.BusinessException;
import com.zff.springboot_demo.exception.ErrorCode;
import com.zff.springboot_demo.user.service.UserService;
import com.zff.springboot_demo.util.TokenUtil;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器：只负责接收请求、调 Service、返回响应。
 * 业务逻辑全部在 UserService 中。
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public Result<LoginResponse> login(@RequestBody LoginRequest request) {
        return Result.success("登录成功", userService.login(request));
    }

    @GetMapping("/me")
    public Result<LoginResponse> me(@RequestHeader("Authorization") String token) {
        return Result.success("获取成功", userService.getMe(token));
    }

    @PostMapping("/logout")
    public Result<Void> logout(@RequestHeader("Authorization") String token) {
        if (!TokenUtil.isValidBearerHeader(token)) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "未登录或 token 无效");
        }
        userService.logout(token);
        return Result.success("退出成功");
    }
}
