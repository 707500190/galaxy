package com.sun.content.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sun.content.api.common.utils.R;
import com.sun.content.api.dto.CrawlerBaseDTO;
import com.sun.content.api.dto.CrawlerReportDTO;
import com.sun.content.api.entity.CrawlerStrategy;
import com.sun.content.api.entity.CrawlerStrategyAccount;
import com.sun.content.service.CrawlerStrategyAccountService;
import com.sun.content.service.CrawlerStrategyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * @描述：控制类
 * @作者: sunshilong
 * @日期: 2022-03-14
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/crawler/strategy")
@Api(value = "CrawlerStrategyController", tags = {""})
public class CrawlerStrategyController {

    public final CrawlerStrategyService crawlerStrategyService;
    public final CrawlerStrategyAccountService accountService;

    /**
     * 爬虫策略保存
     *
     * @param dto 爬虫策略保存
     * @return 爬虫策略保存
     */
    @ApiOperation(value = "爬虫策略保存")
    @PostMapping("save")
    public R<String> save(@RequestBody CrawlerBaseDTO dto) {
        crawlerStrategyService.saveStrategyAndAccounts(dto);
        return R.ok("success");
    }

    /**
     * 更新
     *
     * @param dto 爬虫策略更新
     * @return 爬虫策略保存
     */
    @ApiOperation(value = "爬虫策略更新")
    @PostMapping("update")
    public R<String> update(@RequestBody CrawlerBaseDTO dto) {
        crawlerStrategyService.updateStrategyAndAccounts(dto);
        return R.ok("success");
    }

    /**
     * 爬虫策略列表
     *
     * @param dto 爬虫策略列表
     * @return 爬虫策略列表
     */
    @ApiOperation(value = "爬虫策略列表")
    @PostMapping("page")
    public R<IPage<CrawlerStrategy>> list(@RequestBody CrawlerReportDTO dto) {
        Page<CrawlerStrategy> page = new Page<>();
        page.setCurrent(dto.getPage());
        page.setSize(dto.getPageSize());
        page.setPages(dto.getPageSize());
        IPage<CrawlerStrategy> list = crawlerStrategyService.page(page);
        return R.ok(list);
    }

    /**
     * 爬虫策略保存
     *
     * @param id 主键
     * @return 爬虫策略详情
     */
    @ApiOperation(value = "爬虫策略详情")
    @GetMapping("detail")
    public R<CrawlerBaseDTO> getById(@RequestParam("id") Long id) {
        CrawlerBaseDTO dto = crawlerStrategyService.getStrategyById(id);
        return R.ok(dto);
    }

    /**
     * 爬虫策略保存
     *
     * @param id 主键
     * @return 爬虫策略详情
     */
    @ApiOperation(value = "爬虫策略删除")
    @GetMapping("delete")
    public R<String> delete(@RequestParam("id") Long id) {
        crawlerStrategyService.removeById(id);
        accountService.remove(new QueryWrapper<CrawlerStrategyAccount>().eq("strategy_id", id));
        return R.ok("success");
    }


}