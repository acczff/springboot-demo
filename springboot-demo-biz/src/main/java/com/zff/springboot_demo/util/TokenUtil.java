package com.zff.springboot_demo.util;

import java.util.Optional;

/**
 * Token 工具类
 * 统一管理 token 的生成、Bearer 校验和用户 ID 提取逻辑。
 */
public final class TokenUtil {

    private static final String TOKEN_PREFIX = "token-";
    private static final String BEARER_PREFIX = "Bearer ";

    private TokenUtil() {
    }

    /**
     * 生成 token，格式：token-{userId}-{timestamp}
     */
    public static String generateToken(Long userId) {
        return TOKEN_PREFIX + userId + "-" + System.currentTimeMillis();
    }

    /**
     * 判断 Authorization 请求头是否满足 Bearer token-xxx 格式。
     */
    public static boolean isValidBearerHeader(String authorizationHeader) {
        return authorizationHeader != null && authorizationHeader.startsWith(BEARER_PREFIX + TOKEN_PREFIX);
    }

    /**
     * 从 Authorization 请求头中提取 userId。
     * 不合法时抛出 IllegalArgumentException。
     */
    public static Long extractUserId(String authorizationHeader) {
        if (!isValidBearerHeader(authorizationHeader)) {
            throw new IllegalArgumentException("invalid authorization header");
        }

        String tokenValue = authorizationHeader.substring(BEARER_PREFIX.length());
        String[] parts = tokenValue.split("-");
        if (parts.length < 3) {
            throw new IllegalArgumentException("malformed token");
        }

        try {
            return Long.parseLong(parts[1]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("invalid user id in token", e);
        }
    }

    /**
     * 安全提取 userId，token 不合法时返回空。
     */
    public static Optional<Long> tryExtractUserId(String authorizationHeader) {
        if (!isValidBearerHeader(authorizationHeader)) {
            return Optional.empty();
        }

        String tokenValue = authorizationHeader.substring(BEARER_PREFIX.length());
        String[] parts = tokenValue.split("-");
        if (parts.length < 3) {
            return Optional.empty();
        }

        try {
            return Optional.of(Long.parseLong(parts[1]));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
}
