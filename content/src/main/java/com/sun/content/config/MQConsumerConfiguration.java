package com.sun.content.config;

import com.sun.content.exception.RocketMQException;
import com.sun.content.config.processor.MQConsumeConcurrentlyListenerProcessor;
import com.sun.content.config.processor.MQConsumeOrderlyListenerProcessor;
import com.sun.content.config.processor.MQMsgProcessor;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * RocketMQ 消费者配置
 */
@Data
//@Component
@ConditionalOnProperty(value = "rocketmq.consumer.enable")
@ConfigurationProperties(prefix = "rocketmq.consumer")
public class MQConsumerConfiguration {

    public static final Logger logger = LoggerFactory.getLogger(MQConsumerConfiguration.class);

    private String namesrvAddr;

    private String groupName;

    private int consumeThreadMin;

    private int consumeThreadMax;

    private String topics;

    private int consumeMessageBatchMaxSize;

    private String messageModel = "CLUSTERING";

    private Boolean orderlyConsume = false;

    @Autowired
    private Map<String, MQMsgProcessor> mqMsgProcessorServiceMap;

    @Bean
    public DefaultMQPushConsumer getRocketMQConsumer() throws RocketMQException {
        if (StringUtils.isEmpty(this.groupName)) {
            throw new RocketMQException("200001", "groupName is blank");
        }
        if (StringUtils.isEmpty(this.namesrvAddr)) {
            throw new RocketMQException("200002", "nameServerAddr is blank");
        }
        if (StringUtils.isEmpty(topics)) {
            throw new RocketMQException("200003", "topics is null");
        }

        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(groupName);

        consumer.setNamesrvAddr(namesrvAddr);
        consumer.setConsumeThreadMin(consumeThreadMin);
        consumer.setConsumeThreadMax(consumeThreadMax);
        if (orderlyConsume) {
            consumer.registerMessageListener(new MQConsumeOrderlyListenerProcessor(mqMsgProcessorServiceMap));
            consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        } else {
            consumer.registerMessageListener(new MQConsumeConcurrentlyListenerProcessor(mqMsgProcessorServiceMap));
            consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
        }
        consumer.setConsumeMessageBatchMaxSize(consumeMessageBatchMaxSize);
        if (MessageModel.BROADCASTING.getModeCN().equals(messageModel)) {
            consumer.setMessageModel(MessageModel.BROADCASTING);
        }
        try {
            /**
             * 设置该消费者订阅的主题和tag，如果是订阅该主题下的所有tag，则tag使用*；如果需要指定订阅该主题下的某些tag，则使用||分割，例如tag1||tag2||tag3
             */
            String[] topicTagsArr = topics.split(";");
            if (null != topicTagsArr && topicTagsArr.length > 0) {
                for (String topicTags : topicTagsArr) {
                    String[] topicTag = topicTags.split("~");
                    consumer.subscribe(topicTag[0], topicTag[1]);
                }
            }
            consumer.start();
            logger.info("consumer is start !!! groupName:{},topics:{},namesrvAddr:{}", groupName, topics, namesrvAddr);
        } catch (MQClientException e) {
            logger.error("consumer is start !!! groupName:{},topics:{},namesrvAddr:{}", groupName, topics, namesrvAddr, e);
            throw new RocketMQException(e);
        }
        return consumer;
    }

}