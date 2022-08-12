package com.sun.content.service.impl;

//import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.content.api.entity.Tiktok;
import com.sun.content.mapper.TiktokMapper;
import com.sun.content.service.TiktokService;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import static com.sun.content.api.common.constant.Constant.DYNAMIC_DB_CRAWLER;

/**
 * @描述： 服务类
 * @作者: sunshilong
 * @日期: 2022-05-23
 */
@Service
@RequiredArgsConstructor
////@DS(DYNAMIC_DB_CRAWLER)
public class TiktokServiceImpl extends ServiceImpl<TiktokMapper, Tiktok> implements TiktokService{


}