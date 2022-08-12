package com.sun.content.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sun.content.api.common.utils.R;
import com.sun.content.api.dto.WeiXinOfficialAccountQueryDTO;
import com.sun.content.api.entity.WeixinOfficialAccount;
import com.sun.content.service.WeixinOfficialAccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @描述：微信公众号控制类
 * @作者: tw
 * @日期: 2022-02-23
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/weixinOfficialAccount")
@Api(value = "WeixinOfficialAccountController", tags = {"微信公众号"})
public class WeixinOfficialAccountController {

    public final WeixinOfficialAccountService weixinOfficialAccountService;

    /**
     * 微信公众号查询
     *
     * @param weiXinOfficialAccountQueryDTO 微信公众号分页查询参数
     * @return 微信公众号分页对象
     */
    @ApiOperation(value = "微信公众号分页查询")
    @GetMapping("/page")
    public R<Page<WeixinOfficialAccount>> page(Page page, WeiXinOfficialAccountQueryDTO weiXinOfficialAccountQueryDTO) {
        return R.ok(weixinOfficialAccountService.page(page, weiXinOfficialAccountQueryDTO));
    }

}