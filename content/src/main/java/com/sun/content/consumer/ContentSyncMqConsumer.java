package com.sun.content.consumer;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.sun.content.api.common.constant.CommonConstants;
import com.sun.content.api.dto.ContentSyncDTO;
import com.sun.content.config.processor.AbstractMQMsgProcessor;
import com.sun.content.config.processor.MQConsumeResult;
import com.sun.content.service.ContentSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 内容消息消费者
 */
//@Component
@Slf4j
@RequiredArgsConstructor
public class ContentSyncMqConsumer extends AbstractMQMsgProcessor {

    private final ContentSyncService syncService;
    @Value("${index.name}")
    private String INDEX_NAME;


    @Override
    public MQConsumeResult consumeMessage(String tag, List<String> keys, MessageExt messageExt) {
        MQConsumeResult result = new MQConsumeResult();
        int reConsume = messageExt.getReconsumeTimes();
        // 消息已经重试了3次，如果不需要再次消费，则返回成功
        if (reConsume == 3) {
            result.setSuccess(true);
            log.warn("retry more than 3 times !");
            return result;
        }
        handle(messageExt);
        result.setSuccess(true);
        return result;
    }


    @Override
    public String getTopic() {
        return CommonConstants.CONTENT_TOPIC;
    }

    @Override
    public List<String> getTags() {
        return Arrays.asList("*");
    }

    private void handle(MessageExt messageExt) {
        String messageBody = new String(messageExt.getBody());
        if (StringUtils.isNotBlank(messageBody)) {
            try {
                ContentSyncDTO syncDTO = JSONObject.parseObject(messageBody, ContentSyncDTO.class);
                if (Objects.nonNull(syncDTO)) {
                    syncService.syncDbToESByIdsAndKeys(syncDTO.getIds(), INDEX_NAME, Lists.newArrayList(syncDTO.getChannel()), 1000);
                }
            } catch (Exception e) {
                log.error("-----------爬虫内容同步错误--------------{}", e.getMessage());
            }
        }
    }
}
