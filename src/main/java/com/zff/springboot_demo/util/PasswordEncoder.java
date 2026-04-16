package com.zff.springboot_demo.util;


import org.mindrot.jbcrypt.BCrypt;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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

    // 读取文本文件的所有行
    public static List<String> readFile(String fileName) {
        List<String> result = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {
            while (bufferedReader.ready()) {
                result.add(bufferedReader.readLine());
            }
        } catch (IOException e){
            throw new RuntimeException("读取文件失败：", e);
        }
        return result;
    }

//    // 新方式（Files）
//    public static String readFile(String path) throws IOException {
//        return Files.readString(Paths.get(path));  // Java 11+
//    }

    // 写入文本到文件
    public static void writeFile(String fileName, List<String> result) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName))) {
            for (String line : result) {
                bufferedWriter.write(line);
            }
        } catch (IOException e){
            throw new RuntimeException("写入文件失败：", e);
        }
    }

    // 复制文件（字节流）
    public static void copyFile(String source, String target){
        try(FileInputStream input = new FileInputStream(source);
        FileOutputStream output = new FileOutputStream(target)){
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
        } catch (IOException e){
            throw new RuntimeException("复制文件失败：", e);
        }
    }

//    // 测试方法
//    public static void main(String[] args) throws InterruptedException{
////        String rawPassword = "654321";
////        String encodedPassword = encode(rawPassword); //
////        System.out.println("加密后的密码：" + encodedPassword);
////        System.out.println("验证成功：" + matches(rawPassword, encodedPassword));
////        System.out.println("验证失败：" + matches("wrong", encodedPassword));
//        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(
//                2,
//                4,
//                60,
//                TimeUnit.SECONDS,
//                new ArrayBlockingQueue<>(10),
//                new ThreadPoolExecutor.CallerRunsPolicy()
//        );
//        System.out.println("=== 开始提交 10 个任务 ===\n");
//        for (int i = 0; i < 50; i++) {
//            int taskId = i;
//            System.out.println("【提交任务 " + taskId + "】");
//            poolExecutor.submit(() -> {
//                System.out.println("  → " + Thread.currentThread().getName() +
//                        " 开始执行任务 " + taskId);
//                try {
//                    Thread.sleep(1000);  // 模拟耗时 1 秒
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                System.out.println("  → " + Thread.currentThread().getName() +
//                        " 完成任务 " + taskId);
//            });
//            Thread.sleep(200);  // 每 0.2 秒提交一个任务
//        }
//        System.out.println("\n=== 所有任务已提交 ===");
//        poolExecutor.shutdown();
//    }

}
