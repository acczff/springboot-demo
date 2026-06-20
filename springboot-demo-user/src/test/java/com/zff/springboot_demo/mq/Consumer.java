package com.zff.springboot_demo.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

/**
 * 最小消费者：不管消息是怎么发出来的，只要队列里有，就拿出来处理。
 */
public class Consumer {
    private static final String QUEUE_NAME = "hello_queue";

    public static void main(String[] args) throws Exception {
        // 1. 创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);

        // 2. 建立连接并开启通道（注意：消费者需要持续监听，所以不能用 try-with-resources 自动关闭连接）
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // 3. 声明队列（消费者也声明一次，防止先启动消费者时队列不存在报错）
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println(" [*] 等待接收消息。按 CTRL+C 退出");

        // 4. 定义如何处理收到的消息（回调函数）
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [消费者] 收到信件: '" + message + "'");
        };

        // 5. 开始消费（参数：队列名，是否自动确认，接收消息的回调，取消消费的回调）
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });
    }
}
