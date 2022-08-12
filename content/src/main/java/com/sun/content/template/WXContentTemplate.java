package com.sun.content.template;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sun.content.api.entity.WeixinOfficialAccount;
import com.sun.content.service.WeixinOfficialAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.sun.content.api.common.constant.Constant.WX_ACCOUNT;

/**
 * 微信公众号查询模板
 *
 * @author sunshilong
 */
@Component(WX_ACCOUNT)
public class WXContentTemplate extends AbstractContentQueryTemplate<WeixinOfficialAccount> {

    @Autowired
    private WeixinOfficialAccountService queryService;


    @Override
    protected IService<WeixinOfficialAccount> getQueryService() {
        return queryService;
    }

    @Override
    protected Class<WeixinOfficialAccount> getType() {
        return WeixinOfficialAccount.class;
    }
}
