package com.sun.content.mapper;

import com.sun.content.api.dto.CrawlerAccountDTO;
import com.sun.content.api.entity.CrawlerStrategyAccount;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @描述： Mapper 接口
 * @作者: sunshilong
 * @日期: 2022-03-14
 */
@Mapper
public interface CrawlerStrategyAccountMapper extends BaseMapper<CrawlerStrategyAccount> {

    void saveAccountBatch(@Param("dto") CrawlerAccountDTO dto);

}