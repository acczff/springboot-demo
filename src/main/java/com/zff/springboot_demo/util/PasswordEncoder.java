package com.zff.springboot_demo.util;


import org.mindrot.jbcrypt.BCrypt;

/**
 * 密码加密工具类
 * 使用 BCrypt 算法进行密码加密和验证
 */
public class PasswordEncoder {

    /**
     * 加密密码
     * @param rawPassword 明文密码
     * @return 加密后的密码
     */
    public static String encode(String rawPassword) {
        // 使用 BCrypt 加密，gensalt() 生成随机盐
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt());
    }

    /**
     * 验证密码
     * @param rawPassword 明文密码
     * @param encodedPassword 加密后的密码
     * @return 是否匹配
     */
    public static boolean matches(String rawPassword, String encodedPassword) {
        // BCrypt 自动处理盐的验证
        return BCrypt.checkpw(rawPassword, encodedPassword);
    }

//    // 测试方法
//    public static void main(String[] args) {
//        String rawPassword = "654321";
//        String encodedPassword = encode(rawPassword); //
//        System.out.println("加密后的密码：" + encodedPassword);
//        System.out.println("验证成功：" + matches(rawPassword, encodedPassword));
//        System.out.println("验证失败：" + matches("wrong", encodedPassword));
//    }

}
