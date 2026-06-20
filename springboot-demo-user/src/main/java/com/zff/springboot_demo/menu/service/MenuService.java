package com.zff.springboot_demo.menu.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zff.springboot_demo.menu.entity.Menu;
import com.zff.springboot_demo.menu.repository.MenuRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
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
            // 第一次检查：缓存有就直接返回（99% 的请求到这就结束了，不加锁）
            String cached = redisTemplate.opsForValue().get(key);
            if (cached != null) {
                return objectMapper.readValue(cached, new TypeReference<List<Menu>>() {});
            }

            // ========== 缓存未命中，进入击穿防护（DCL） ==========
            String lockKey = key + ":lock";
            Boolean locked = redisTemplate.opsForValue()
                    .setIfAbsent(lockKey, "1", 5, TimeUnit.SECONDS);  // SETNX，5秒自动释放防死锁

            if (Boolean.TRUE.equals(locked)) {
                try {
                    // 第二次检查：等锁期间别人可能已经写好了缓存
                    cached = redisTemplate.opsForValue().get(key);
                    if (cached != null) {
                        return objectMapper.readValue(cached, new TypeReference<List<Menu>>() {});
                    }
                    // 真正查 DB 并写缓存（只有拿到锁的这一个请求会走到这里）
                    List<Menu> menus = getMenuTree(userRoles);
                    String json = objectMapper.writeValueAsString(menus);
                    long jitter = menus.isEmpty() ? ThreadLocalRandom.current().nextInt(30)
                            : ThreadLocalRandom.current().nextInt(600);
                    long ttl = menus.isEmpty() ? 60 : 3600;  // 空值缓存60s防穿透，正常数据1h
                    redisTemplate.opsForValue().set(key, json, ttl + jitter, TimeUnit.SECONDS);
                    return menus;
                } finally {
                    redisTemplate.delete(lockKey);  // 释放锁
                }
            } else {
                // 没拿到锁 → 等 50ms 后再查缓存（拿到锁的人应该已经写好了）
                Thread.sleep(50);
                cached = redisTemplate.opsForValue().get(key);
                if (cached != null) {
                    return objectMapper.readValue(cached, new TypeReference<List<Menu>>() {});
                }
                // 兜底：锁超时或写缓存失败，降级直接查 DB
                return getMenuTree(userRoles);
            }
        } catch (Exception e) {
            // 降级：Redis 宕机或序列化异常，直接查 DB
            return getMenuTree(userRoles);
        }
    }

    public void evictMenuCache() {
        // 菜单数据变更时调用，删除所有角色的菜单缓存（模式匹配）
        // TODO: 当前还没有菜单 CRUD 接口，后续新增/修改/删除菜单时必须调用它。
        var keys = redisTemplate.keys("menus:tree:*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }
}
