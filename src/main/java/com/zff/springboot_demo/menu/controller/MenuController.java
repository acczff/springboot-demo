package com.zff.springboot_demo.menu.controller;

import com.zff.springboot_demo.Result;
import com.zff.springboot_demo.menu.entity.Menu;
import com.zff.springboot_demo.menu.repository.MenuRepository;
import com.zff.springboot_demo.menu.service.MenuService;
import com.zff.springboot_demo.role.entity.Role;
import com.zff.springboot_demo.user.service.UserService;
import com.zff.springboot_demo.user.entity.User;
import org.springframework.web.bind.annotation.RequestHeader;
import java.util.Collections;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/menus")
public class MenuController {

    private final MenuService menuService;
    private final UserService userService;

    public MenuController(MenuService menuService, UserService userService) {
        this.menuService = menuService;
        this.userService = userService;
    }

    @GetMapping
    public Result<List<Menu>> getMenuTree(@RequestHeader(value = "Authorization", required = false) String token) {

        List<String> userRoles = Collections.emptyList();

        if (token != null && token.startsWith("Bearer token-")) {
            try {
                String[] parts = token.replace("Bearer ", "").split("-");
                Long userId = Long.parseLong(parts[1]);
                User user = userService.findById(userId);
                if (user != null) {
                    userRoles = user.getRoles().stream()
                            .map(Role::getName)
                            .collect(java.util.stream.Collectors.toList());
                }
            } catch (Exception ignored) {}
        }

        List<Menu> menus = menuService.getMenuTree(userRoles);
        return Result.success("menus getMenuTree success", menus);
    }
}
