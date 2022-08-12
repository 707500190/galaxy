package com.sun.content.service;

import com.sun.content.api.dto.CrawlerReportDTO;
import com.sun.content.api.entity.CrawlerAccountReport;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sun.content.api.vo.CrawlerReportVO;
import com.sun.content.api.vo.SearchDataResult;

import java.util.List;

/**
 * @描述： 服务类
 * @作者: sunshilong
 * @日期: 2022-05-19
 */
public interface CrawlerAccountReportService extends IService<CrawlerAccountReport> {

    void saveReportBatch(List<CrawlerReportVO> res);

    SearchDataResult<CrawlerReportVO> pageReport(CrawlerReportDTO dto);
}