package com.zff.springboot_demo.menu.service;

import com.zff.springboot_demo.menu.entity.Menu;
import com.zff.springboot_demo.menu.repository.MenuRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuService {

    private final MenuRepository menuRepository;

    public MenuService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public List<Menu> getMenuTree(List<String> userRoles) {
        List<Menu> allMenus = menuRepository.findAll();

        // 过滤：requiredRole 为 null（所有人可见）或用户拥有该角色
        List<Menu> visibleMenus = allMenus.stream()
                .filter(m -> m.getRequiredRole() == null
                        || userRoles.contains(m.getRequiredRole()))
                .toList();

        // 组装树
        List<Menu> roots = visibleMenus.stream()
                .filter(m -> m.getParentId() == null)
                .toList();

        for (Menu root : roots) {
            List<Menu> children = visibleMenus.stream()
                    .filter(m -> root.getId().equals(m.getParentId()))
                    .toList();
            root.setChildren(children);
        }
        return roots;
    }
}
