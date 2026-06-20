package com.zff.springboot_demo.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Day 153 实战演练：Redis 缓存穿透、击穿、雪崩
 * (改为 Controller 以避开测试环境 JDK 底层的 ClassCircularityError Bug)
 */
@RestController
@RequestMapping("/cache-drill")
public class CacheDrillController {

    private static final Logger log = LoggerFactory.getLogger(CacheDrillController.class);

    @Autowired
    private StringRedisTemplate redisTemplate;

    // 模拟慢查询
    private String mockDatabaseQuery(String id, AtomicInteger dbHitCount) {
        dbHitCount.incrementAndGet();
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        if (id.equals("-999")) return null;
        return "WorkOrderData_" + id;
    }

    @GetMapping("/penetration")
    public String testCachePenetration() {
        String badId = "-999";
        String cacheKey = "workorder:" + badId;
        AtomicInteger dbHits = new AtomicInteger(0);

        redisTemplate.delete(cacheKey);
        log.info("=== 开始缓存穿透测试 ===");
        
        for (int i = 0; i < 100; i++) {
            String cacheValue = redisTemplate.opsForValue().get(cacheKey);
            if (cacheValue != null) {
                if ("<NULL>".equals(cacheValue)) continue;
                continue; 
            }
            String dbResult = mockDatabaseQuery(badId, dbHits);
            if (dbResult == null) {
                redisTemplate.opsForValue().set(cacheKey, "<NULL>", 5, TimeUnit.MINUTES);
            } else {
                redisTemplate.opsForValue().set(cacheKey, dbResult, 30, TimeUnit.MINUTES);
            }
        }
        
        String result = "缓存穿透测试结束。模拟 100 次恶意查询。数据库实际被击中次数: " + dbHits.get();
        log.info(result);
        return result + " (如果没用缓存空值，这里应该是 100)";
    }

    @GetMapping("/breakdown")
    public String testCacheBreakdown() throws InterruptedException {
        String hotKeyId = "1001";
        String cacheKey = "workorder:" + hotKeyId;
        AtomicInteger dbHits = new AtomicInteger(0);
        
        redisTemplate.delete(cacheKey);
        log.info("=== 开始缓存击穿测试 (使用 DCL 双重锁) ===");
        
        int threadCount = 200;
        ExecutorService threadPool = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            threadPool.submit(() -> {
                try {
                    String cacheValue = redisTemplate.opsForValue().get(cacheKey);
                    if (cacheValue == null) {
                        synchronized (this) {
                            cacheValue = redisTemplate.opsForValue().get(cacheKey);
                            if (cacheValue == null) {
                                String dbResult = mockDatabaseQuery(hotKeyId, dbHits);
                                redisTemplate.opsForValue().set(cacheKey, dbResult, 30, TimeUnit.MINUTES);
                            }
                        }
                    }
                } finally {
                    latch.countDown();
                }
            });
        }
        
        latch.await();
        threadPool.shutdown();
        
        String result = "缓存击穿测试结束。200 个并发线程同时冲击。数据库实际被击中次数: " + dbHits.get();
        log.info(result);
        return result + " (如果不加锁，这里应该是 200)";
    }

    @GetMapping("/avalanche")
    public String testCacheAvalanche() {
        log.info("=== 开始缓存雪崩测试 ===");
        Random random = new Random();
        StringBuilder sb = new StringBuilder("雪崩测试演示完成。所有的 Key 将在不同时刻错峰过期。<br>");
        
        for (int i = 0; i < 10; i++) {
            String cacheKey = "workorder:batch:" + i;
            int baseTTL = 30;
            int jitterTTL = random.nextInt(6); 
            int finalTTL = baseTTL + jitterTTL;
            
            redisTemplate.opsForValue().set(cacheKey, "Data_" + i, finalTTL, TimeUnit.MINUTES);
            String logMsg = String.format("写入 Key: %s, 过期时间: %d 分钟 (含随机抖动 %d 分钟)", cacheKey, finalTTL, jitterTTL);
            log.info(logMsg);
            sb.append(logMsg).append("<br>");
        }
        return sb.toString();
    }
}
