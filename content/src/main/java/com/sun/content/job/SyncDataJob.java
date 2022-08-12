package com.sun.content.job;

import com.sun.content.service.ContentSyncService;
import com.sun.content.service.CrawlerStrategyAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * @author sunshilong
 * @version 1.0
 * @date 2022/3/11
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class SyncDataJob {

    private final ContentSyncService syncService;

    private final CrawlerStrategyAccountService accountService;

    @Value("${index.name}")
    private String IndexName;

//    @XxlJob(value = "contentSyncToES")
//    public ReturnT<String> syncESByBatch(String param) {
//
//        Calendar cal = Calendar.getInstance();
//        cal.add(Calendar.DATE, -1);
//        Date start = cal.getTime();
//        Date end = new Date();
//
//        //默认时间，当前时间到昨天此时的时间段；
//        syncService.syncDbToESByDateAndKeys(IndexName, start, end, 1000, null);
//        log.info("--------------爬虫内容数据同步ES-------------------");
//        return ReturnT.SUCCESS;
//    }


}
