package com.sun.content.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sun.content.api.dto.CrawlerReportDTO;
import com.sun.content.api.entity.CrawlerAccountReport;
import com.sun.content.api.vo.CrawlerReportVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @描述： Mapper 接口
 * @作者: sunshilong
 * @日期: 2022-05-19
 */
@Mapper
public interface CrawlerAccountReportMapper extends BaseMapper<CrawlerAccountReport> {

    void saveReportBatch(@Param("list") List<CrawlerReportVO> list);

    IPage<CrawlerReportVO> pageReport(@Param("page") Page page, @Param("dto")CrawlerReportDTO dto);


}