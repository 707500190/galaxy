package com.sun.content.mapper;

//import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sun.content.api.dto.ReptileContentQueryDTO;
import com.sun.content.api.entity.RedContent;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sun.content.api.vo.ReptilePageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

import static com.sun.content.api.common.constant.Constant.DYNAMIC_DB_CRAWLER;

/**
 * @描述：小红书主体 Mapper 接口
 * @作者: tw
 * @日期: 2022-02-23
 */
@Mapper
//@DS(DYNAMIC_DB_CRAWLER)
public interface RedContentMapper extends BaseMapper<RedContent> {

    IPage<ReptilePageVO> allPage(@Param("page") Page page,@Param("reptileContentQueryDTO") ReptileContentQueryDTO reptileContentQueryDTO);

    IPage<ReptilePageVO> page1(@Param("page") Page page,@Param("reptileContentQueryDTO") ReptileContentQueryDTO reptileContentQueryDTO);
    IPage<ReptilePageVO> page2(@Param("page") Page page,@Param("reptileContentQueryDTO") ReptileContentQueryDTO reptileContentQueryDTO);
    List selectFromScrap();
}