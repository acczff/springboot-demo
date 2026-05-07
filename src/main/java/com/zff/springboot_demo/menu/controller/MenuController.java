package com.zff.springboot_demo.menu.controller;

import com.zff.springboot_demo.Result;
import com.zff.springboot_demo.menu.entity.Menu;
import com.zff.springboot_demo.menu.repository.MenuRepository;
import com.zff.springboot_demo.menu.service.MenuService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/menus")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping
    public Result<List<Menu>> getMenuTree() {
        List<Menu> menus = menuService.getMenuTree();
        return Result.success("menus getMenuTree success",menus);
    }
}
