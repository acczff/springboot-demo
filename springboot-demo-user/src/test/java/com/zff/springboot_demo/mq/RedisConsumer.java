package com.zff.springboot_demo.mq;

import io.lettuce.core.KeyValue;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

public class RedisConsumer {
    private static final String QUEUE_NAME = "hello_redis_queue";

    public static void main(String[] args) {
        // 1. 连接 Redis
        RedisClient redisClient = RedisClient.create("redis://localhost:6380");
        StatefulRedisConnection<String, String> connection = redisClient.connect();
        RedisCommands<String, String> commands = connection.sync();

        System.out.println(" [*] 等待接收消息。按 CTRL+C 退出");

        // 2. 消费者需要一直死循环监听
        while (true) {
            // BRPOP (Block Right Pop): 阻塞式从列表右侧弹出消息。
            // 0 表示无限等待，如果没有消息，程序会卡在这里“睡觉”，直到有消息进来才醒来。
            // 这就是 MQ 解耦的核心机制！
            KeyValue<String, String> message = commands.brpop(0, QUEUE_NAME);
            
            if (message != null) {
                System.out.println(" [消费者] 收到信件: '" + message.getValue() + "'");
            }
        }
    }
}
