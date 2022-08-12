package com.sun.content.config.processor;

import com.sun.content.exception.RocketMQException;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 生产者发送消息处理器
 */
public class MQProducerSendMsgProcessor {

    private static final Logger logger = LoggerFactory.getLogger(MQProducerSendMsgProcessor.class);

    private static final String EMPTY_ARRAY = "[]";

    private DefaultMQProducer defaultMQProducer;

    public MQProducerSendMsgProcessor(DefaultMQProducer defaultMQProducer) {
        this.defaultMQProducer = defaultMQProducer;
    }

    /**
     * 同步发送消息
     *
     * @param topic 主题
     * @param tag   消息标签，只支持设置一个Tag（服务端消息过滤使用）
     * @param msg   消息内容
     * @return
     */
    public MQSendResult send(String topic, String tag, String msg) {
        return this.send(topic, tag, null, msg, null);
    }

    /**
     * 同步发送消息
     *
     * @param topic 主题
     * @param tag   消息标签，只支持设置一个Tag（服务端消息过滤使用）
     * @param msg   消息内容
     * @param routingKey 消息路由键
     * @return
     */
    public MQSendResult send(String topic, String tag, String msg, String routingKey) {
        return this.send(topic, tag, null, msg, routingKey);
    }

    /**
     * 同步发送消息
     *
     * @param topic 主题
     * @param tag   消息标签，只支持设置一个Tag（服务端消息过滤使用）
     * @param keys  消息关键词，多个Key用MessageConst.KEY_SEPARATOR隔开（查询消息使用）
     * @param msg   消息内容
     * @return
     */
    public MQSendResult send(String topic, String tag, String keys, String msg, String routingKey) {
        logger.info(String.format("发送信息到消息队列。topic:%s,tag:%s,keys:%s,msg:%s", topic == null ? "" : topic,
                tag == null ? "" : tag, keys == null ? null : keys, msg));
        MQSendResult mqSendResult;
        try {
            validateSendMsg(topic, tag, msg);
            Message sendMsg = new Message(topic, StringUtils.isEmpty(tag) ? null : tag, StringUtils.isEmpty(keys) ? null : keys, msg.getBytes());
            SendResult sendResult;
            if (StringUtils.isNotBlank(routingKey)) {
                sendResult = defaultMQProducer.send(sendMsg, new MessageQueueSelector() {
                    @Override
                    public MessageQueue select(List<MessageQueue> list, Message message, Object o) {
                        Integer queusNum = Math.abs(o.hashCode() % list.size());
                        return list.get(queusNum);
                    }
                }, routingKey);
            } else {
                //默认3秒超时
                sendResult = defaultMQProducer.send(sendMsg);
            }
            mqSendResult = new MQSendResult(sendResult);
        } catch (MQClientException | RemotingException | MQBrokerException | InterruptedException e) {
            logger.error("消息发送失败", e);
            mqSendResult = new MQSendResult("消息发送失败", e);
        }
        logger.info("发送消息到消息队列的响应信息为：" + mqSendResult.toString());
        return mqSendResult == null ? new MQSendResult() : mqSendResult;
    }

    /**
     * 异步发送消息
     *
     * @param topic 主题
     * @param tag   消息标签，只支持设置一个Tag（服务端消息过滤使用）
     * @param msg   消息内容
     * @return
     */
    public void asyncSend(String topic, String tag, String msg, String routingKey, SendCallback sendCallback) {
        logger.info(String.format("发送信息到消息队列。topic:%s,tag:%s,msg:%s", topic == null ? "" : topic,
                tag == null ? "" : tag, msg));
        try {
            validateSendMsg(topic, tag, msg);
            Message sendMsg = new Message(topic, StringUtils.isEmpty(tag) ? null : tag, msg.getBytes());
            if (StringUtils.isNotBlank(routingKey)) {
                defaultMQProducer.send(sendMsg, (list, message, o) -> {
                    Integer queueNum = Math.abs(o.hashCode() % list.size());
                    return list.get(queueNum);
                }, routingKey);
            } else {
                //默认3秒超时
                defaultMQProducer.send(sendMsg,sendCallback);
            }
        } catch (MQClientException | RemotingException | MQBrokerException | InterruptedException e) {
            logger.error("消息发送失败", e);
        }
    }

    /**
     * 参数校验
     *
     * @param topic
     * @param tag
     * @param msg
     */
    private void validateSendMsg(String topic, String tag, String msg) {
        if (topic == null) {
            throw new RocketMQException("110001", "topic为空");
        }
        if (tag == null) {
            throw new RocketMQException("110002", "tag为空");
        }
        if (msg == null || EMPTY_ARRAY.equals(msg)) {
            throw new RocketMQException("110003", "msg为空");
        }
    }

}
