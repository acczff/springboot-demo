package com.zff.springboot_demo.menu.service;

import com.zff.springboot_demo.menu.entity.Menu;
import com.zff.springboot_demo.menu.repository.MenuRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {

    private final MenuRepository menuRepository;

    public MenuService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public List<Menu> getMenuTree() {
        // 从数据库取出所有菜单（平铺列表）
        List<Menu> allMenus = menuRepository.findAll();
        // 第一步：找出所有根节点（parentId 为 null）
        List<Menu> roots = allMenus.stream()
                .filter(m -> m.getParentId() == null)
                .toList();
        // 第二步：给每个根节点找子菜单
        for (Menu root : roots) {
            List<Menu> children = allMenus.stream()
                    .filter(m -> root.getId().equals(m.getParentId()))
                    .toList();
            root.setChildren(children);
        }
        return roots;
    }
}
