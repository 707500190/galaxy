package com.sun.content.config.processor;

import com.alibaba.fastjson.JSON;
import com.sun.content.exception.RocketMQException;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class AbstractMQConsumeProcessor {

    protected static final Logger logger = LoggerFactory.getLogger(AbstractMQConsumeProcessor.class);

    protected Map<String, MQMsgProcessor> mqMsgProcessorServiceMap;

    public AbstractMQConsumeProcessor(Map<String, MQMsgProcessor> mqMsgProcessorServiceMap) {
        this.mqMsgProcessorServiceMap = mqMsgProcessorServiceMap;
    }


    protected void handle(List<MessageExt> msgs) throws Exception {
        //根据Topic分组
        Map<String, List<MessageExt>> topicGroups = msgs.stream().collect(Collectors.groupingBy(MessageExt::getTopic));
        for (Map.Entry<String, List<MessageExt>> topicEntry : topicGroups.entrySet()) {
            String topic = topicEntry.getKey();
            //根据tags分组
            List<MessageExt> value = topicEntry.getValue();
            for (MessageExt messageExt : value) {
                if(StringUtils.isBlank(messageExt.getTags())){
                    messageExt.setTags("*");
                }
            }
            Map<String, List<MessageExt>> tagGroups = topicEntry.getValue().stream().collect(Collectors.groupingBy(MessageExt::getTags));
            for (Map.Entry<String, List<MessageExt>> tagEntry : tagGroups.entrySet()) {
                String tag = tagEntry.getKey();
                //消费某个主题下，tag的消息
                consumeMsgForTag(topic, tag, tagEntry.getValue());
            }
        }

    }

    /**
     * 根据topic和tags路由，查找消费消息服务
     *
     * @param topic
     * @param tag
     * @param value
     */
    protected void consumeMsgForTag(String topic, String tag, List<MessageExt> value) throws Exception {
        //根据topic 和  tag查询具体的消费服务
        Optional<Map.Entry<String, MQMsgProcessor>> first = mqMsgProcessorServiceMap.entrySet().stream().filter(item -> topic.equals(item.getValue().getTopic()) && (item.getValue().getTags().contains("*") || item.getValue().getTags().contains(tag))).findFirst();
        try {
            if (!first.isPresent()) {
                logger.error(String.format("根据Topic：%s和Tag:%s 没有找到对应的处理消息的服务", topic, tag));
                throw new RocketMQException("000001", "没有找到对应的消息处理服务");
            }
            MQMsgProcessor imqMsgProcessor = first.get().getValue();
            logger.info(String.format("根据Topic：%s和Tag:%s 路由到的服务为:%s，开始调用处理消息", topic, tag, imqMsgProcessor.getClass().getName()));
            //调用该类的方法,处理消息
            MQConsumeResult mqConsumeResult = imqMsgProcessor.handle(topic, tag, value);
            if (mqConsumeResult == null) {
                throw new RocketMQException("000002", "消费结果返回对象为空");
            }
            if (mqConsumeResult.isSuccess()) {
                logger.info("消息处理成功：" + JSON.toJSONString(mqConsumeResult));
            } else {
                throw new RocketMQException("000003", "消息消费失败");
            }
            if (mqConsumeResult.isSaveConsumeLog()) {
                logger.debug("开始记录消费日志");
                //TODO 记录消费日志
            }
        } catch (Exception e) {
            throw new RocketMQException(e.getLocalizedMessage());
        }
    }
}
