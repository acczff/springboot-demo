package com.zff.springboot_demo.controller;

import com.zff.springboot_demo.Result;
import com.zff.springboot_demo.dto.login.LoginRequest;
import com.zff.springboot_demo.dto.login.LoginResponse;
import com.zff.springboot_demo.user.entity.User;
import com.zff.springboot_demo.user.service.UserService;
import com.zff.springboot_demo.util.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 * 处理登录、退出等认证相关请求
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    UserService userService;

    /**
     * 登录接口 V2（数据库校验版本）
     * @param request 登录请求（账号密码）
     * @return 登录响应（token 和用户信息）
     */
    @PostMapping("/login")
    public Result<LoginResponse> login(@RequestBody LoginRequest request){
        // 1. 根据账号查询用户
        // 根据账号（用户名或邮箱）查询用户
        // 暂时使用用户名查询
        User user = userService.findByUsername(request.getAccount());
        // 是否能通过用户名查到数据
        if(user == null){
            return Result.error(401,"帐号不存在");
        }
        // 判断密码是否正确
        boolean matches = PasswordEncoder.matches(request.getPassword(), user.getPassword());
        if(!matches){
            return Result.error(401,"密码错误");
        }

        // 验证成功返回数据
        LoginResponse response = new LoginResponse();
        response.setToken("token-" + user.getId() + "-" + System.currentTimeMillis());
        response.setUserId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        return Result.success("登录成功",response);
    }

    /**
     * 获取当前用户信息
     * @param token 登录令牌（从 Header 中获取）
     * @return 当前用户信息
     */
    @GetMapping("/me")
    public Result<LoginResponse> me(@RequestHeader("Authorization") String token){
        if(token == null || !token.startsWith("Bearer token-")){
            return Result.error(404,"未登录或 token 失效");
        }
        try {
            // 2. 尝试无情拆解 Token
            String tokenValue = token.replace("Bearer ", "");
            String[] parts = tokenValue.split("-");

            if (parts.length < 2) {
                return Result.error(404, "Token 被篡改");
            }

            // 提取用户 ID
            Long userId = Long.parseLong(parts[1]);
            User user = userService.findById(userId);

            // 3. 查无此人，说明原来登录过但号被库里删了，必须返回 401 让他回登录页，而不是 404
            if(user == null){
                return Result.error(404, "账号已失效，请重新登录");
            }

            // 4. 一切顺利，成功交差
            LoginResponse response = new LoginResponse();
            response.setUserId(user.getId());
            response.setUsername(user.getUsername());
            response.setEmail(user.getEmail());
            return Result.success("获取成功", response);

        } catch (Exception e) {
            // 5. 终极兜底：比如强转数字报错，绝不崩掉报 500，而是温柔地返回 401
            return Result.error(404, "Token 解析异常，请重新登录");
        }
    }

    /**
     * 退出登录
     * @param token 登录令牌（从 Header 中获取）
     * @return 退出结果
     */
    @PostMapping("/logout")
    public Result<Void> logout(@RequestHeader("Authorization") String token){
        if(token == null || !token.startsWith("Bearer token-")){
            return Result.error(401,"未登陆或 token 无效");
        }
        return Result.success("退出成功");
    }
}
