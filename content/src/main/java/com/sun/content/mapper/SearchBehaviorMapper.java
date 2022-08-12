package com.sun.content.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sun.content.api.entity.SearchBehavior;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @描述： Mapper 接口
 * @作者: sunshilong
 * @日期: 2022-05-07
 */
@Mapper
public interface SearchBehaviorMapper extends BaseMapper<SearchBehavior> {

    void saveOrUpdateHistory(@Param("value") String value, @Param("type") String type);

}