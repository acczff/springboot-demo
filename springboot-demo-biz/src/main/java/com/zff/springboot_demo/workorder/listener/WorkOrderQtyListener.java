package com.zff.springboot_demo.workorder.listener;

import com.zff.springboot_demo.config.RabbitMQConfig;
import com.zff.springboot_demo.workorder.repository.WorkOrderItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Component
public class WorkOrderQtyListener {

    private static final Logger log = LoggerFactory.getLogger(WorkOrderQtyListener.class);

    private final WorkOrderItemRepository workOrderItemRepository;

    public WorkOrderQtyListener(WorkOrderItemRepository workOrderItemRepository) {
        this.workOrderItemRepository = workOrderItemRepository;
    }

    /**
     * 监听队列，处理报工审核通过事件
     */
    @RabbitListener(queues = RabbitMQConfig.QUEUE_WORKORDER_QTY)
    @Transactional
    public void handleWorkReportApproved(Map<String, Object> msg) {
        try {
            Long workOrderItemId = ((Number) msg.get("workOrderItemId")).longValue();
            Integer reportedQty = ((Number) msg.get("reportedQty")).intValue();

            log.info("[MQ 消费者] 收到审核通过消息，开始后台累加产品明细 {} 的数量：+{}", workOrderItemId, reportedQty);

            // 模拟一段耗时的复杂更新逻辑（比如发邮件、算库存），证明主线程不会被阻塞
            Thread.sleep(1000); 

            workOrderItemRepository.findById(workOrderItemId).ifPresent(item -> {
                item.setCompletedQty(item.getCompletedQty() + reportedQty);
                workOrderItemRepository.save(item);
                log.info("[MQ 消费者] 累加完成！当前总数量：{}", item.getCompletedQty());
            });

        } catch (Exception e) {
            log.error("[MQ 消费者] 处理审核通过消息失败，丢弃或转入死信队列", e);
            // 真实项目中可能会抛出 AmqpRejectAndDontRequeueException 丢入死信队列
        }
    }
}
