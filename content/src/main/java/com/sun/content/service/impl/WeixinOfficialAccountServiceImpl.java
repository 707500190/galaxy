package com.sun.content.service.impl;

//import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.content.api.dto.WeiXinOfficialAccountQueryDTO;
import com.sun.content.api.entity.WeixinOfficialAccount;
import com.sun.content.mapper.WeixinOfficialAccountMapper;
import com.sun.content.service.WeixinOfficialAccountService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import static com.sun.content.api.common.constant.Constant.DYNAMIC_DB_CRAWLER;


/**
 * @描述：微信公众号 服务类
 * @作者: tw
 * @日期: 2022-02-23
 */
@Service
@RequiredArgsConstructor
////@DS(DYNAMIC_DB_CRAWLER)
public class WeixinOfficialAccountServiceImpl extends ServiceImpl<WeixinOfficialAccountMapper, WeixinOfficialAccount> implements WeixinOfficialAccountService {


    @Override
    public Page<WeixinOfficialAccount> page(Page page, WeiXinOfficialAccountQueryDTO weiXinOfficialAccountQueryDTO) {
        return this.page(page, new LambdaQueryWrapper<WeixinOfficialAccount>()
                .ge(StringUtils.isNotBlank(weiXinOfficialAccountQueryDTO.getStartDate()), WeixinOfficialAccount::getPublishTime, weiXinOfficialAccountQueryDTO.getStartDate())
                .le(StringUtils.isNotBlank(weiXinOfficialAccountQueryDTO.getEndDate()), WeixinOfficialAccount::getPublishTime, weiXinOfficialAccountQueryDTO.getEndDate())
                .and(StringUtils.isNotBlank(weiXinOfficialAccountQueryDTO.getKeyword()), wrapper -> wrapper
                        .like(WeixinOfficialAccount::getAccount, weiXinOfficialAccountQueryDTO.getKeyword())
                        .or()
                        .like(WeixinOfficialAccount::getArticleContent, weiXinOfficialAccountQueryDTO.getKeyword())
                        .or()
                        .like(WeixinOfficialAccount::getTitle, weiXinOfficialAccountQueryDTO.getKeyword()))
        );
    }
}