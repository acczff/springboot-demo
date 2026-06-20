package com.zff.springboot_demo.mq;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

public class RedisProducer {
    private static final String QUEUE_NAME = "hello_redis_queue";

    public static void main(String[] args) throws Exception {
        // 1. 连接到本地正在运行的 Redis (根据你的 docker-compose.yml，映射的是 6380 端口)
        RedisClient redisClient = RedisClient.create("redis://localhost:6380");
        StatefulRedisConnection<String, String> connection = redisClient.connect();
        RedisCommands<String, String> commands = connection.sync();

        // 2. 发送 5 条消息
        for (int i = 1; i <= 5; i++) {
            String message = "Hello Redis MQ! 这是第 " + i + " 条消息";
            // 用 LPUSH (Left Push) 把消息推入列表左侧，这就相当于发信
            commands.lpush(QUEUE_NAME, message);
            System.out.println(" [生产者] 发送完毕: '" + message + "'");
            Thread.sleep(1000);
        }

        connection.close();
        redisClient.shutdown();
    }
}
