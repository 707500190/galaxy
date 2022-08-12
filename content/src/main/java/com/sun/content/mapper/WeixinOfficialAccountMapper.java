package com.sun.content.mapper;

//import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sun.content.api.entity.WeixinOfficialAccount;
import org.apache.ibatis.annotations.Mapper;

import static com.sun.content.api.common.constant.Constant.DYNAMIC_DB_CRAWLER;

/**
 * @描述：微信公众号 Mapper 接口
 * @作者: tw
 * @日期: 2022-02-23
 */
@Mapper
//@DS(DYNAMIC_DB_CRAWLER)
public interface WeixinOfficialAccountMapper extends BaseMapper<WeixinOfficialAccount> {

}