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
    public static final String QUEUE_WORKORDER_QTY = "workorder.qty.queue.v2";
    public static final String ROUTING_KEY_WORKREPORT_APPROVED = "workreport.approved";

    @Bean
    public DirectExchange mesExchange() {
        return new DirectExchange(EXCHANGE_MES, true, false);
    }

    @Bean
    public Queue workOrderQtyQueue() {
        return new Queue(QUEUE_WORKORDER_QTY, true, false, false);
    }

    @Bean
    public Binding bindingWorkOrderQtyQueue(Queue workOrderQtyQueue, DirectExchange mesExchange) {
        return BindingBuilder.bind(workOrderQtyQueue).to(mesExchange).with(ROUTING_KEY_WORKREPORT_APPROVED);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
