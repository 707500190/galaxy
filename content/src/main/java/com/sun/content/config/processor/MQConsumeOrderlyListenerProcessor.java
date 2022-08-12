package com.sun.content.config.processor;

import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * 消费者消费消息路由
 */
public class MQConsumeOrderlyListenerProcessor extends AbstractMQConsumeProcessor implements MessageListenerOrderly {

    private static final Logger logger = LoggerFactory.getLogger(MQConsumeOrderlyListenerProcessor.class);

    public MQConsumeOrderlyListenerProcessor(Map<String, MQMsgProcessor> mqMsgProcessorServiceMap) {
        super(mqMsgProcessorServiceMap);
    }

    @Override
    public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext consumeOrderlyContext) {
        consumeOrderlyContext.setAutoCommit(true);
        ConsumeOrderlyStatus consumeOrderlyStatus = ConsumeOrderlyStatus.SUCCESS;
        if (CollectionUtils.isEmpty(msgs) || mqMsgProcessorServiceMap == null) {
            logger.info("接受到的消息为空，不处理，直接返回成功");
            return consumeOrderlyStatus;
        }

        try {
            handle(msgs);
        } catch (Exception e) {
            logger.error("处理消息失败", e);
            consumeOrderlyStatus = ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
        }
        return consumeOrderlyStatus;
    }
}