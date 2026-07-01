package com.zff.springboot_demo.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zff.springboot_demo.config.TokenBlacklistService;
import com.zff.springboot_demo.dto.login.LoginRequest;
import com.zff.springboot_demo.dto.login.LoginResponse;
import com.zff.springboot_demo.exception.BusinessException;
import com.zff.springboot_demo.exception.ErrorCode;
import com.zff.springboot_demo.permission.entity.Permission;
import com.zff.springboot_demo.role.entity.Role;
import com.zff.springboot_demo.role.repository.RoleRepository;
import com.zff.springboot_demo.user.entity.User;
import com.zff.springboot_demo.user.repository.UserRepository;
import com.zff.springboot_demo.util.PasswordEncoder;
import com.zff.springboot_demo.util.TokenUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * 用户业务逻辑层
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TokenBlacklistService tokenBlacklistService;
    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, String> redisTemplate;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, TokenBlacklistService tokenBlacklistService, ObjectMapper objectMapper, RedisTemplate<String, String> redisTemplate) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.tokenBlacklistService = tokenBlacklistService;
        this.objectMapper = objectMapper;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 根据 ID 查找用户
     * @param id 用户 ID
     * @return 用户对象
     */
    public User findById(Long id)    {
        return userRepository.findWithRolesById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "用户不存在"));
    }

    /**
     * 判断用户是否为 ADMIN 角色。
     */
    public boolean isAdmin(Long userId) {
        User user = findById(userId);
        return user.getRoles().stream()
                .anyMatch(role -> "ADMIN".equalsIgnoreCase(role.getName()));
    }

    /**
     * 根据 username 查找用户
     * @param username 用户名
     * @return 用户对象
     */
    public User findByUsername(String username) {
        return userRepository.findWithRolesByUsername(username);
    }

    /**
     * 查找所有用户
     * @return 用户列表
     */
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }


    /**
     * 按关键字分页查询用户；关键字为空时查询全部。
     */
    public Page<User> findAll(String keyword, Pageable pageable) {
        if (keyword == null || keyword.isBlank()) {
            return userRepository.findAll(pageable);  // 没有关键词，查全部
        }
        return userRepository.findByUsernameContainingOrEmailContaining(
                keyword, keyword, pageable
        );
    }
    /**
     * 创建用户
     * @param user 用户对象
     * @return 创建后的用户对象
     */
    public User createUser(User user) {
        user.setPassword(PasswordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    /**
     * 更新用户
     * @param id 用户 ID
     * @param user 用户对象
     * @return 更新后的用户对象，不存在返回 null
     */
    public User updateUser(Long id, User user) {
        User existingUser = userRepository.findById(id).orElse(null);
        if (existingUser == null) return null;
        existingUser.setUsername(user.getUsername());
        existingUser.setEmail(user.getEmail());
        if (user.getPassword() != null && !user.getPassword().isBlank()) {
            existingUser.setPassword(PasswordEncoder.encode(user.getPassword()));
        }
        User saved = userRepository.save(existingUser);
        evictUserCache(id);  // 用户名/邮箱变了，缓存失效
        return saved;
    }

    /**
     * 删除用户
     * @param id 用户 ID
     * @return 是否删除成功
     */

    public boolean deleteUser(Long id) {
        if(!userRepository.existsById(id)) {
            return false;
        }
        userRepository.deleteById(id);
        return true;
    }

    /**
     * 查询用户已绑定的角色列表。
     */
    public List<Role> getUserRoles(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "用户不存在"));
        return user.getRoles();
    }

    /**
     * 给用户绑定角色，传入的角色 ID 列表会覆盖原有绑定。
     */
    @Transactional
    public User assignRoles(Long userId, List<Long> roleIds) {
        User user = userRepository.findById(userId).
                orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "用户不存在"));
        List<Role> roles = roleRepository.findAllById(roleIds);
        user.setRoles(roles);
        User saved = userRepository.save(user);
        evictUserCache(userId);  // 角色权限变了，缓存失效
        return saved;
    }

    /**
     * 登录校验：账号密码验证 + Redis 限速防暴力破解。
     *
     * 限速规则：
     * - 同一账号 5 分钟内连续 5 次密码错误 → 锁定，返回 429
     * - 登录成功 → 清除失败计数
     */
    private static final String LOGIN_FAIL_PREFIX = "login:fail:";
    private static final int MAX_FAIL_COUNT = 5;           // 最大允许失败次数
    private static final int LOCK_MINUTES = 5;             // 锁定时间（分钟）

    public LoginResponse login(LoginRequest request) {
        String failKey = LOGIN_FAIL_PREFIX + request.getAccount();

        // 1. 先检查是否已被锁定
        String failCountStr = redisTemplate.opsForValue().get(failKey);
        int failCount = (failCountStr != null) ? Integer.parseInt(failCountStr) : 0;
        if (failCount >= MAX_FAIL_COUNT) {
            throw new BusinessException(ErrorCode.TOO_MANY_REQUESTS,
                    "登录失败次数过多，请 " + LOCK_MINUTES + " 分钟后重试");
        }

        // 2. 查用户
        User user = findByUsername(request.getAccount());
        if (user == null) {
            incrementFailCount(failKey);
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "账号不存在");
        }

        // 3. 校验密码
        if (!PasswordEncoder.matches(request.getPassword(), user.getPassword())) {
            incrementFailCount(failKey);
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "密码错误");
        }

        // 4. 登录成功 → 清除失败计数
        redisTemplate.delete(failKey);
        return buildLoginResponse(user, TokenUtil.generateToken(user.getId()));
    }

    /**
     * 失败计数 +1，首次失败时设置过期时间。
     */
    private void incrementFailCount(String failKey) {
        Long count = redisTemplate.opsForValue().increment(failKey);
        if (count != null && count == 1) {
            // 第一次失败，设置过期时间
            redisTemplate.expire(failKey, LOCK_MINUTES, TimeUnit.MINUTES);
        }
    }

    /**
     * 退出登录：把 token 加入 Redis 黑名单，24 小时内不可再用。
     */
    public void logout(String token) {
        String rawToken = token.substring("Bearer ".length());
        tokenBlacklistService.blacklist(rawToken, 86400);
    }

    /**
     * 获取当前登录用户信息（token 校验 + 用户查询）。
     */
    public LoginResponse getMe(String token) {
        Optional<Long> userId = TokenUtil.tryExtractUserId(token);
        if (userId.isEmpty()) throw new BusinessException(ErrorCode.UNAUTHORIZED, "未登录或 token 失效");

        return getUserFromCache(userId.get());
    }

    /**
     * 组装登录响应（login 和 getMe 共用）。
     * token 为 null 时不填充 token 字段（me 接口不需要重新签发）。
     */
    private LoginResponse buildLoginResponse(User user, String token) {
        LoginResponse response = new LoginResponse();
        if (token != null) response.setToken(token);
        response.setUserId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        List<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());
        response.setRoles(roleNames);
        List<String> permissions = user.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(Permission::getCode)
                .distinct()
                .collect(Collectors.toList());
        response.setPermissions(permissions);
        return response;
    }

    private LoginResponse getUserFromCache(Long userId) {
        String key = "user:info:" + userId;
        String cached = redisTemplate.opsForValue().get(key);
        try {
            if (cached == null) {
                // cache miss：查 DB，写入 Redis
                User user = findById(userId);
                LoginResponse resp = buildLoginResponse(user, null);
                String json = objectMapper.writeValueAsString(resp);
                long ttl = 1800 + ThreadLocalRandom.current().nextInt(300);
                redisTemplate.opsForValue().set(key, json, ttl, TimeUnit.SECONDS);
                return resp;
            } else {
                // cache hit：直接反序列化返回
                return objectMapper.readValue(cached, LoginResponse.class);
            }
        } catch (JsonProcessingException e) {
            // 降级：序列化失败，直接查 DB
            User user = findById(userId);
            return buildLoginResponse(user, null);
        }
    }

    public void evictUserCache(Long userId) {
        redisTemplate.delete("user:info:" + userId);
    }
}
