package com.sun.content.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sun.content.api.dto.WeiXinOfficialAccountQueryDTO;
import com.sun.content.api.entity.WeixinOfficialAccount;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @描述：微信公众号 服务类
 * @作者: tw
 * @日期: 2022-02-23
 */
public interface WeixinOfficialAccountService extends IService<WeixinOfficialAccount> {

    Page<WeixinOfficialAccount> page(Page page, WeiXinOfficialAccountQueryDTO weiXinOfficialAccountQueryDTO);
}