package com.sun.content.mapper;

//import com.baomidou.dynamic.datasource.annotation.DS;
import com.sun.content.api.dto.CrawlerReportDTO;
import com.sun.content.api.entity.CrawlerStrategy;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sun.content.api.vo.CrawlerReportVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

import static com.sun.content.api.common.constant.Constant.DYNAMIC_DB_CRAWLER;

/**
 * @描述： Mapper 接口
 * @作者: sunshilong
 * @日期: 2022-03-14
 */
@Mapper
public interface CrawlerStrategyMapper extends BaseMapper<CrawlerStrategy> {

//    //@DS(DYNAMIC_DB_CRAWLER)
    List<CrawlerReportVO> listReportRed(@Param("dto") CrawlerReportDTO dto);

//    //@DS(DYNAMIC_DB_CRAWLER)
    List<CrawlerReportVO> listReportWX(@Param("dto") CrawlerReportDTO dto);

//    //@DS(DYNAMIC_DB_CRAWLER)
    List<CrawlerReportVO> listReportTiktok(@Param("dto")CrawlerReportDTO dto);
}