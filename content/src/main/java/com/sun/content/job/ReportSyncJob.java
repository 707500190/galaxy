package com.sun.content.job;

import com.sun.content.api.common.utils.DateUtil;
import com.sun.content.api.dto.CrawlerReportDTO;
import com.sun.content.service.CrawlerStrategyAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author sunshilong
 * @version 1.0
 * @date 2022/5/25
 */
@RequiredArgsConstructor
@Component
@Slf4j
public class ReportSyncJob {

    private final CrawlerStrategyAccountService accountService;

    //每天晚上7点执行一次,因为上线XXLJOB坏了，暂时先用这个，后面替换XXLJOB
    @Scheduled(cron = "0 0 19 * * ?")
    public void syncReport() {
        CrawlerReportDTO dto = new CrawlerReportDTO();
        Date now = new Date();
        //上个月到现在；
        dto.setMonthStart(DateUtil.getDateOfLastMonth());
        dto.setMonthEnd(now);
        //去年到现在；
        dto.setYearStart(DateUtil.getDayOfLastYear());
        dto.setYearEnd(now);
        //默认时间，当前时间到昨天此时的时间段；
        accountService.syncAccountReport(dto);
        log.info("--------------统计账号抓取报表到mysql-------------------");
    }
}
