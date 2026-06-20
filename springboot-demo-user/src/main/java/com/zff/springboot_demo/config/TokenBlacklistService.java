package com.zff.springboot_demo.config;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class TokenBlacklistService {

    private final RedisTemplate<String, String> redisTemplate;

    public TokenBlacklistService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // 把 token 加入黑名单，TTL 秒后自动过期
    public void blacklist(String token, long ttlSeconds) {
        redisTemplate.opsForValue().set("blacklist:" + token, "1", ttlSeconds, TimeUnit.SECONDS);
    }

    // 检查 token 是否在黑名单里
    public boolean isBlacklisted(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey("blacklist:" + token));
    }
}
