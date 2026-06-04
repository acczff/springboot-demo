package com.zff.springboot_demo.menu.controller;

import com.zff.springboot_demo.Result;
import com.zff.springboot_demo.menu.entity.Menu;
import com.zff.springboot_demo.menu.service.MenuService;
import com.zff.springboot_demo.role.entity.Role;
import com.zff.springboot_demo.util.TokenUtil;
import com.zff.springboot_demo.user.service.UserService;
import com.zff.springboot_demo.user.entity.User;
import org.springframework.web.bind.annotation.RequestHeader;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 菜单管理接口
 */
@RestController
@RequestMapping("/api/menus")
public class MenuController {

    private final MenuService menuService;
    private final UserService userService;

    public MenuController(MenuService menuService, UserService userService) {
        this.menuService = menuService;
        this.userService = userService;
    }

    /**
     * 获取当前用户可见的菜单树
     * @param token 登录令牌
     * @return 菜单树
     */
    @GetMapping
    public Result<List<Menu>> getMenuTree(@RequestHeader(value = "Authorization", required = false) String token) {

        List<String> userRoles = Collections.emptyList();

        Optional<Long> userId = TokenUtil.tryExtractUserId(token);
        if (userId.isPresent()) {
            User user = userService.findById(userId.get());
            if (user != null) {
                userRoles = user.getRoles().stream()
                        .map(Role::getName)
                        .collect(java.util.stream.Collectors.toList());
            }
        }

        List<Menu> menus = menuService.getMenuTreeCached(userRoles);
        return Result.success("menus getMenuTreeCached success", menus);
    }
}
