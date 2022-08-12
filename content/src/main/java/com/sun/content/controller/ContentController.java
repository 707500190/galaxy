package com.sun.content.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.sun.content.api.common.utils.ProcessByStep;
import com.sun.content.api.dto.ContentSyncDTO;
import com.sun.content.api.entity.ContentInfo;
import com.sun.content.api.vo.ContentInfoVO;
import com.sun.content.api.vo.SearchDataResult;
import com.sun.content.config.processor.MQProducerSendMsgProcessor;
import com.sun.content.exception.CheckedException;
import com.sun.content.search.SearchOption;
import com.sun.content.service.ContentSearchService;
import com.sun.content.service.ContentSyncService;
import com.sun.content.api.common.utils.R;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.sun.content.api.common.constant.CommonConstants.CONTENT_TOPIC;
import static com.sun.content.api.dto.SyncTypeEnum.ASYNC;

/**
 * 内容相关接口类
 *
 * @author sunshilong
 * @version 1.0
 * @date 2022/3/8
 */
//@RestController
@RequestMapping("/content")

@RequiredArgsConstructor
@Slf4j
public class ContentController {

    private final ContentSearchService searchService;

    private final ContentSyncService syncService;

//    private final DefaultMQProducer mqProducer;

    @Value("${index.name}")
    private String INDEX_NAME;
    //几个Id为一个消息
    @Value("${rocketmq.producer.msgBatch}")
    private Integer msgBatch;

    /**
     * 内容分页查询
     *
     * @param searchOption 查询参数
     * @return 结果集
     */
    @ApiOperation(value = "ES内容分页查询")
    @PostMapping("search/page")
    public R<SearchDataResult<ContentInfoVO>> page(@RequestBody SearchOption searchOption) {
        if (Objects.isNull(searchOption)) {
            searchOption = new SearchOption();
        }
        searchOption.setIndex(INDEX_NAME);
        return R.ok(searchService.page(searchOption));
    }

    /**
     * 内容单条详情
     *
     * @param id（es） 文档主键
     * @return 结果集
     */
    @ApiOperation(value = "ES详情查询")
    @GetMapping("search/detail")
    public R<ContentInfo> getDetailById(@RequestParam("id") String id, @RequestParam("keyword") String keyword) {
        return R.ok(searchService.getDetailById(INDEX_NAME, id, keyword));
    }

    /**
     * 内容单条详情
     *
     * @return 结果集
     */
    @ApiOperation(value = "ES详情查询")
    @GetMapping("sync")
    public R getDetailBdyId(@RequestParam String index, @RequestParam List<String> channelList) {
        syncService.syncDbToESByDateAndKeys(index, null, new Date(), 5000, channelList);
        return R.ok();
    }

    /**
     * 内容单条详情
     *
     * @return 结果集
     */
    @ApiOperation(value = "ES详情查询")
    @GetMapping("syncTest")
    public R getDetailBdyIdd(@RequestParam String key) {
        syncService.syncDbToESByIdsAndKeys(Lists.newArrayList(1L), "think_tank", Lists.newArrayList(key), 5000);
        return R.ok();
    }

    @ApiOperation(value = "接收爬虫传递的内容消息到MQ")
    @PostMapping("sync/mq")
    public R syncMq(@RequestBody ContentSyncDTO dto) {
//        try {
//            if (Objects.isNull(dto) || CollectionUtils.isEmpty(dto.getIds()) || StringUtils.isEmpty(dto.getChannel())) {
//                throw new CheckedException(20005, "参数不可为空！");
//            }
//            MQProducerSendMsgProcessor processor = new MQProducerSendMsgProcessor(mqProducer);
//            //同一渠道，每msgBatch个id一组作为一个消息发往队列；
//            ProcessByStep<Long> step = new ProcessByStep<>(dto.getIds(), msgBatch);
//            step.execute(s -> {
//                try {
//                    ObjectMapper objectMapper = new ObjectMapper();
//                    ContentSyncDTO syncDTO = new ContentSyncDTO();
//                    syncDTO.setChannel(dto.getChannel());
//                    syncDTO.setIds(s);
//                    String objStr = objectMapper.writeValueAsString(syncDTO);
//                    //异步
//                    if (dto.getSyncType() != null && dto.getSyncType().equals(ASYNC)) {
//                        processor.asyncSend(CONTENT_TOPIC, "*", objStr, null, null);
//                        log.info("内容异步走队列。。。，message:{}", objStr);
//                    }
//                    //默认同步发送
//                    else {
//                        processor.send(CONTENT_TOPIC, "*", null, objStr, null);
//                        log.info("内容同步走队列。。。，message:{}", objStr);
//                    }
//
//                } catch (IOException e) {
//                    log.error("sync mq ,mapper value error :", e);
//                }
//            });
//            return R.ok(null, "success");
//        } catch (Exception e) {
//            log.error("爬虫传递的内容消息到MQ失败：", e);
//            throw new CheckedException(20004, "爬虫传递的内容消息到MQ失败!");
//        }
        return null;
    }


}
