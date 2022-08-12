package com.sun.content.config;

import com.sun.content.config.processor.MQProducerSendMsgProcessor;
import com.sun.content.exception.RocketMQException;
import lombok.Data;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


/**
 * RocketMQ 生产者配置
 */
@Data
//@Component
@ConditionalOnProperty(value = "rocketmq.producer.enable")
@ConfigurationProperties(prefix = "rocketmq.producer")
public class MQProducerConfiguration {

    public static final Logger logger = LoggerFactory.getLogger(MQProducerConfiguration.class);

    private String groupName;

    private String namesrvAddr;

    private Integer maxMessageSize;

    private Integer sendMsgTimeout;

    private Integer retryTimesWhenSendFailed;

    @Bean
    public DefaultMQProducer getRocketMQProducer() throws RocketMQException {
        if (org.apache.commons.lang3.StringUtils.isEmpty(this.groupName)) {
            throw new RocketMQException("100001", "groupName is blank");
        }
        if (org.apache.commons.lang3.StringUtils.isEmpty(this.namesrvAddr)) {
            throw new RocketMQException("100002", "nameServerAddr is blank");
        }

        DefaultMQProducer producer;
        producer = new DefaultMQProducer(this.groupName);
        producer.setNamesrvAddr(this.namesrvAddr);
        //如果需要同一个jvm中不同的producer往不同的mq集群发送消息，需要设置不同的instanceName
        if (this.maxMessageSize != null) {
            producer.setMaxMessageSize(this.maxMessageSize);
        }
        if (this.sendMsgTimeout != null) {
            producer.setSendMsgTimeout(this.sendMsgTimeout);
        }
        //如果发送消息失败，设置重试次数，默认为2次
        if (this.retryTimesWhenSendFailed != null) {
            producer.setRetryTimesWhenSendFailed(this.retryTimesWhenSendFailed);
        }
        try {
            producer.start();
            logger.info(String.format("producer is start ! groupName:[%s],namesrvAddr:[%s]"
                    , this.groupName, this.namesrvAddr));
        } catch (MQClientException e) {
            logger.error(String.format("producer is error {}", e.getMessage(), e));
            throw new RocketMQException(e);
        }
        return producer;
    }

    @Bean
    public MQProducerSendMsgProcessor mQProducerSendMsgProcessor(DefaultMQProducer defaultMQProducer) {
        return new MQProducerSendMsgProcessor(defaultMQProducer);
    }

}