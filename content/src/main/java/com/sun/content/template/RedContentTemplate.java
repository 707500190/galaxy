package com.sun.content.template;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sun.content.api.dto.QueryContentDTO;
import com.sun.content.api.entity.RedContent;
import com.sun.content.api.entity.Tiktok;
import com.sun.content.service.RedContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.sun.content.api.common.constant.Constant.RED_BOOK;

/**
 * 小红书查询模板
 *
 * @author sunshilong
 */
@Component(RED_BOOK)
public class RedContentTemplate extends AbstractContentQueryTemplate<RedContent> {

    @Autowired
    private RedContentService redContentService;

    @Override
    protected IService<RedContent> getQueryService() {
        return redContentService;
    }

    @Override
    protected Class<RedContent> getType() {
        return RedContent.class;
    }

    @Override
    void parse(RedContent red, QueryContentDTO res){
        //account 应该用账号名称；
        String account = red.getNickName();
        res.setAccount(account);
    }
}
