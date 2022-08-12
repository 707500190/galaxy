package com.sun.content.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.content.api.dto.CrawlerReportDTO;
import com.sun.content.api.entity.CrawlerAccountReport;
import com.sun.content.api.vo.CrawlerReportVO;
import com.sun.content.api.vo.SearchDataResult;
import com.sun.content.mapper.CrawlerAccountReportMapper;
import com.sun.content.service.CrawlerAccountReportService;
import com.sun.content.service.MetadataDicService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.sun.content.util.CommonUtil.getOrderItemByMap;

/**
 * @描述： 服务类
 * @作者: sunshilong
 * @日期: 2022-05-19
 */
@Service
@RequiredArgsConstructor
public class CrawlerAccountReportServiceImpl extends ServiceImpl<CrawlerAccountReportMapper, CrawlerAccountReport> implements CrawlerAccountReportService {

    private final MetadataDicService dicService;

    @Override
    public void saveReportBatch(List<CrawlerReportVO> res) {
        this.baseMapper.saveReportBatch(res);
    }

    @Override
    public SearchDataResult<CrawlerReportVO> pageReport(CrawlerReportDTO dto) {
        Page<CrawlerReportVO> page = new Page<>();
        page.setSize(dto.getPageSize());
        page.setCurrent(dto.getPage());
        Map<String, String> channelMap = dicService.getDicByType("channel");
        page.setOrders(getOrderItemByMap(dto.getSort()));
        IPage<CrawlerReportVO> resPage = this.baseMapper.pageReport(page, dto);
        SearchDataResult<CrawlerReportVO> res = new SearchDataResult<>();
        res.setContent(resPage.getRecords().stream().
                peek(s -> s.setChannel(channelMap.get(s.getChannel()))).collect(Collectors.toList()));
        res.setPages((int) resPage.getPages());
        res.setTotal((int) resPage.getTotal());
        return res;
    }
}