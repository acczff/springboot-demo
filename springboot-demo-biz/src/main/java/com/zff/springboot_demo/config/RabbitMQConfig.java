package com.zff.springboot_demo.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_MES = "mes.exchange";
    public static final String QUEUE_WORKORDER_QTY = "workorder.qty.queue";
    public static final String ROUTING_KEY_WORKREPORT_APPROVED = "workreport.approved";

    // 1. 声明直连交换机
    @Bean
    public DirectExchange mesExchange() {
        return new DirectExchange(EXCHANGE_MES);
    }

    // 2. 声明专门用于更新工单产品数量的队列
    @Bean
    public Queue workOrderQtyQueue() {
        // durable = true 代表持久化，重启 RabbitMQ 后队列依然在
        return new Queue(QUEUE_WORKORDER_QTY, true);
    }

    // 3. 将队列绑定到交换机上，指定路由键
    @Bean
    public Binding bindingWorkOrderQtyQueue(Queue workOrderQtyQueue, DirectExchange mesExchange) {
        return BindingBuilder.bind(workOrderQtyQueue).to(mesExchange).with(ROUTING_KEY_WORKREPORT_APPROVED);
    }

    // 4. 配置 JSON 序列化器（这样发送出去的对象会自动转成 JSON，而不会变成看不懂的 Java 乱码）
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}