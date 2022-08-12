package com.sun.content.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sun.content.api.dto.CrawlerAccountDTO;
import com.sun.content.api.dto.CrawlerBaseDTO;
import com.sun.content.api.dto.CrawlerReportDTO;
import com.sun.content.api.entity.CrawlerStrategyAccount;
import com.sun.content.api.vo.CrawlerReportVO;
import com.sun.content.api.vo.SearchDataResult;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @描述： 爬虫账号
 * @作者: sunshilong
 * @日期: 2022-03-14
 */
public interface CrawlerStrategyAccountService extends IService<CrawlerStrategyAccount> {

    /**
     * 全量保存所有传来的账号，删除库中的旧账号
     *
     */
    void saveAccountBatchAndRemove(List<String> accounts, CrawlerBaseDTO strategy);

    /**
     * 增量保存账号信息；
     *
     */
    void saveAccountBatch(List<String> accounts, CrawlerBaseDTO strategy);

    Map<String, String> getAccountTagMap(Set<String> accountList);

    void saveAccount(CrawlerAccountDTO dto);

    CrawlerBaseDTO getStrategyAccountById(Long id);

    /**
     * 批量保存、更新 账号和标签
     *
     * @param dto 账号信息
     */
    void saveAccountBatchAndRemove(CrawlerAccountDTO dto);

    @Deprecated
    SearchDataResult<CrawlerReportVO> listReport(CrawlerReportDTO dto);

    void syncAccountReport(CrawlerReportDTO dto);

}