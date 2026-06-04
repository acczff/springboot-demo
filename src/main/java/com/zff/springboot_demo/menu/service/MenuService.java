package com.zff.springboot_demo.menu.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zff.springboot_demo.menu.entity.Menu;
import com.zff.springboot_demo.menu.repository.MenuRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 菜单业务逻辑层，负责按角色过滤并组装菜单树。
 */
@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, String> redisTemplate;

    public MenuService(MenuRepository menuRepository, RedisTemplate<String, String> redisTemplate, ObjectMapper objectMapper) {
        this.menuRepository = menuRepository;
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * 获取当前用户可见的一级菜单及其子菜单。
     */
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

    public List<Menu> getMenuTreeCached(List<String> userRoles) {
        String key = "menus:tree:" + String.join(",", userRoles);
        try {
            // ① 查 Redis
            String cached = redisTemplate.opsForValue().get(key);
            if (cached == null) {
                // ② cache miss：查 DB
                List<Menu> menus = getMenuTree(userRoles);

                // ③ 序列化成 JSON 字符串
                String json = objectMapper.writeValueAsString(menus);

                // ④ 写入 Redis，TTL 3600 秒
                redisTemplate.opsForValue().set(key, json, 3600, TimeUnit.SECONDS);

                return menus;
            } else {
                // ⑤ cache hit：JSON 反序列化回 List<Menu>
                // TypeReference 用来在运行时保住泛型信息，List<Menu>.class 写不出来
                return objectMapper.readValue(cached, new TypeReference<List<Menu>>() {});
            }
        } catch (Exception e) {
            // ⑥ 降级：Redis 不可用 或 序列化失败，直接查 DB
            return getMenuTree(userRoles);
        }
    }

    public void evictMenuCache() {
        // 菜单数据变更时调用，删除所有角色的菜单缓存（模式匹配）
        var keys = redisTemplate.keys("menus:tree:*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }
}
