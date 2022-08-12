package com.sun.content.service;

import com.sun.content.api.dto.CrawlerBaseDTO;
import com.sun.content.api.dto.CrawlerReportDTO;
import com.sun.content.api.entity.CrawlerStrategy;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sun.content.api.vo.CrawlerReportVO;
import com.sun.content.api.vo.SearchDataResult;


/**
 * @描述： 服务类
 * @作者: sunshilong
 * @日期: 2022-03-14
 */
public interface CrawlerStrategyService extends IService<CrawlerStrategy> {

    void saveStrategyAndAccounts(CrawlerBaseDTO dto);

    void updateStrategyAndAccounts(CrawlerBaseDTO dto);

    CrawlerBaseDTO getStrategyById(Long id);
}