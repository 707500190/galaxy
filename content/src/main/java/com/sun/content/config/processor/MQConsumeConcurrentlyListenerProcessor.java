package com.sun.content.config.processor;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * 消费者消费消息路由
 */
public class MQConsumeConcurrentlyListenerProcessor extends AbstractMQConsumeProcessor implements MessageListenerConcurrently {

    private static final Logger logger = LoggerFactory.getLogger(MQConsumeConcurrentlyListenerProcessor.class);

    public MQConsumeConcurrentlyListenerProcessor(Map<String, MQMsgProcessor> mqMsgProcessorServiceMap) {
        super(mqMsgProcessorServiceMap);
    }

    /**
     * 默认msgs里只有一条消息，可以通过设置consumeMessageBatchMaxSize参数来批量接收消息<br/>
     * 不要抛异常，如果没有return CONSUME_SUCCESS ，consumer会重新消费该消息，直到return CONSUME_SUCCESS
     *
     * @param msgs
     * @param context
     * @return
     */
    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        if (CollectionUtils.isEmpty(msgs) || mqMsgProcessorServiceMap == null) {
            logger.info("接受到的消息为空，不处理，直接返回成功");
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
        ConsumeConcurrentlyStatus concurrentlyStatus = ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        try {
            handle(msgs);
        } catch (Exception e) {
            logger.error("处理消息失败", e);
            concurrentlyStatus = ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
        // 如果没有return success ，consumer会重新消费该消息，直到return success
        return concurrentlyStatus;
    }
}