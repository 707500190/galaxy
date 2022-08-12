package com.sun.content.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sun.content.api.common.utils.R;
import com.sun.content.api.dto.ReptileContentQueryDTO;
import com.sun.content.api.vo.ReptileContentVO;
import com.sun.content.api.vo.ReptilePageVO;
import com.sun.content.service.RedContentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * @描述：小红书主体控制类
 * @作者: tw
 * @日期: 2022-02-23
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/redContent")
@Api(value = "RedContentController", tags = {"小红书主体"})

public class RedContentController {

    public final RedContentService redContentService;

    /**
     * 爬虫查询
     *
     * @param reptileContentQueryDTO 爬虫分页查询参数
     * @return 小红书分页对象
     */
    @ApiOperation(value = "爬虫分页查询")
    @GetMapping("/page")
    public R<IPage<ReptilePageVO>> page(Page page, ReptileContentQueryDTO reptileContentQueryDTO) {
        return R.ok(redContentService.page(page, reptileContentQueryDTO));
    }

    /**
     * 爬虫详情查询
     *
     * @param channel 爬虫渠道查询
     * @return 爬虫详情查询
     */
    @ApiOperation(value = "爬虫详情查询")
    @GetMapping("/getDetailById")
    public R<ReptileContentVO> getDetailById(@RequestParam("channel") Integer channel, @RequestParam("id") Long id) {
        return R.ok(redContentService.getDetailById(channel, id));
    }


}