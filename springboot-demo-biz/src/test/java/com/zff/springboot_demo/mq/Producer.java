package com.zff.springboot_demo.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 最小生产者：只负责把消息发到 MQ，不关心谁来收。
 */
public class Producer {
    private static final String QUEUE_NAME = "hello_queue";

    public static void main(String[] args) throws Exception {
        // 1. 创建连接工厂并配置（连接到本地 Docker 中的 RabbitMQ）
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672); // RabbitMQ 默认端口

        // 2. 建立连接并开启一个通道
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            // 3. 声明一个队列（如果队列不存在则创建）
            // 参数：队列名，是否持久化，是否排他，是否自动删除，其他属性
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);

            // 4. 发送 5 条消息
            for (int i = 1; i <= 5; i++) {
                String message = "Hello MQ! 这是第 " + i + " 条消息";
                // 发送消息到默认交换机，路由键就是队列名
                channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
                System.out.println(" [生产者] 发送完毕: '" + message + "'");
                Thread.sleep(1000); // 假装每秒发一条
            }
        }
    }
}
