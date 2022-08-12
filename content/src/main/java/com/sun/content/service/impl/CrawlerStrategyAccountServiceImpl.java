package com.sun.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.sun.content.api.dto.CrawlerAccountDTO;
import com.sun.content.api.dto.CrawlerBaseDTO;
import com.sun.content.api.dto.CrawlerReportDTO;
import com.sun.content.api.entity.CrawlerStrategyAccount;
import com.sun.content.api.vo.CrawlerReportVO;
import com.sun.content.api.vo.SearchDataResult;
import com.sun.content.mapper.CrawlerStrategyAccountMapper;
import com.sun.content.mapper.CrawlerStrategyMapper;
import com.sun.content.service.ContentSyncService;
import com.sun.content.service.CrawlerAccountReportService;
import com.sun.content.service.CrawlerStrategyAccountService;
import com.sun.content.service.MetadataDicService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.sun.content.api.common.constant.Constant.RED_BOOK;
import static com.sun.content.api.common.constant.Constant.WX_ACCOUNT;

/**
 * @描述： 服务类
 * @作者: sunshilong
 * @日期: 2022-03-14
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CrawlerStrategyAccountServiceImpl extends ServiceImpl<CrawlerStrategyAccountMapper, CrawlerStrategyAccount> implements CrawlerStrategyAccountService {

    private final MetadataDicService dicService;

    private final CrawlerStrategyMapper crawlerStrategyMapper;

    private final CrawlerAccountReportService reportService;

    @Autowired
    @Lazy
    private ContentSyncService syncService;

    //TODO:加个分布式锁,粒度到渠道；
    //TODO：加事务
    @Override
    public void saveAccountBatchAndRemove(List<String> accounts, CrawlerBaseDTO strategy) {
        saveAccountBatch(accounts, strategy);
        //删除此次去除的账号
        QueryWrapper<CrawlerStrategyAccount> accountWrapper = new QueryWrapper<CrawlerStrategyAccount>().eq("channel", strategy.getChannel());
        accountWrapper.notIn("account", accounts);
        //每次更新的时候先删除一次旧的账户表；
        this.remove(accountWrapper);

    }

    @Override
    public void saveAccountBatch(List<String> accounts, CrawlerBaseDTO strategy) {
        List<String> tags = strategy.getAccountTag();
        //标签（‘健康,时尚’）
        String finalTags = StringUtils.join(tags.toArray(), ",");

        CrawlerAccountDTO accountDTO = new CrawlerAccountDTO();
        BeanUtils.copyProperties(strategy, accountDTO);
        accountDTO.setStringTag(finalTags);
        accountDTO.setAccountList(accounts);
        //批量保存账号信息；
        saveAccountBatchAndRemove(accountDTO);
        //ES更新账号标签；
        syncService.updateESTagByAccount(accounts, finalTags);
    }



    @Override
    public Map<String, String> getAccountTagMap(Set<String> accountList) {
        QueryWrapper<CrawlerStrategyAccount> wrapper = new QueryWrapper<>();
        wrapper.in("account", accountList);
        List<CrawlerStrategyAccount> list = this.list(wrapper);
        return list.stream()
                .collect(HashMap::new, (m, v) -> m.put(v.getAccount(), v.getAccountTag()), HashMap::putAll);
    }

    //TODO:考虑加锁
    @Override
    public void saveAccount(CrawlerAccountDTO dto) {
        //保存account表
        CrawlerStrategyAccount entity = new CrawlerStrategyAccount();
        BeanUtils.copyProperties(dto, entity);
        //标签更新
        if (!CollectionUtils.isEmpty(dto.getAccountTag())){
            String finalTags = StringUtils.join(dto.getAccountTag().toArray(), ",");
            entity.setAccountTag(finalTags);
        }
        entity.setUpdateTime(new Date());
        this.updateById(entity);
        //更新ES TODO:目前是同步更新，如果一个账号的数据很多有效率问题，考虑加队列
        syncService.updateESTagByAccount(Lists.newArrayList(entity.getAccount()), entity.getAccountTag());

    }


    @Override
    public CrawlerBaseDTO getStrategyAccountById(Long id) {
        CrawlerStrategyAccount account = this.getById(id);
        CrawlerBaseDTO dto = new CrawlerBaseDTO();
        BeanUtils.copyProperties(account, dto);
        dto.setAccountTag(dto.getAccountTag());
        return dto;
    }

    @Override
    public void saveAccountBatchAndRemove(CrawlerAccountDTO dto) {
        this.getBaseMapper().saveAccountBatch(dto);
    }


    private List<String> getTagByCodes(List<String> accountList) {
        Map<String, String> accountMap = dicService.getDicByType("accountTag");
        return accountList.stream().map(accountMap::get).collect(Collectors.toList());
    }

    //TODO:后续改为ES的聚合操作；
    @Override
    @Deprecated
    public SearchDataResult<CrawlerReportVO> listReport(CrawlerReportDTO dto) {
        Map<String, String> channelMap = dicService.getDicByType("channel");

        QueryWrapper<CrawlerStrategyAccount> accountWrapper =
                new QueryWrapper<>();
        if (!StringUtils.isEmpty(dto.getChannel())) {
            accountWrapper.eq("channel", dto.getChannel());
        }
        if (!StringUtils.isEmpty(dto.getAccount())) {
            accountWrapper.like("account", dto.getAccount());
        }
        if (!StringUtils.isEmpty(dto.getStatus())) {
            accountWrapper.eq("status", dto.getStatus());
        }

        List<CrawlerStrategyAccount> accounts = list(accountWrapper);

        List<CrawlerReportVO> res;
        String switchChannel = dto.getChannel() == null ? "default" : dto.getChannel();
        switch (switchChannel) {
            case RED_BOOK:
                res = crawlerStrategyMapper.listReportRed(dto);
                break;
            case WX_ACCOUNT:
                res = crawlerStrategyMapper.listReportWX(dto);
                break;
            default:
                res = crawlerStrategyMapper.listReportRed(dto);
                res.addAll(crawlerStrategyMapper.listReportWX(dto));
                break;
        }
        //爬虫数据报表
        List<String> countReports = res.stream().map(CrawlerReportVO::getAccount).collect(Collectors.toList());
        Map<String, CrawlerReportVO> reportMap = res.stream().collect(HashMap::new, (m, v) -> m.put(v.getAccount(), v), HashMap::putAll);

        //最终合并结果
        List<CrawlerReportVO> itemList = Lists.newArrayList();
        //为结果集中添加爬虫库中不存在的账号，
        for (CrawlerStrategyAccount s : accounts) {
            CrawlerReportVO vo;
            //此处过滤掉智库系统中已删除的账号
            if (countReports.contains(s.getAccount())) {
                vo = reportMap.get(s.getAccount());
            } else {
                vo = new CrawlerReportVO();
                vo.setAccount(s.getAccount());
                vo.setChannel(s.getChannel());
                vo.setCountMonth(0);
                vo.setCountYear(0);
            }
            vo.setAccountId(s.getId());
            vo.setStatus(s.getStatus());
            itemList.add(vo);
        }
        //获得流
        Stream<CrawlerReportVO> tempStream = itemList.stream();
        //排序
        Map<String, Boolean> sortMap = dto.getSort();
        if (!CollectionUtils.isEmpty(sortMap)) {
            Comparator<CrawlerReportVO> comparator = null;
            for (String key : sortMap.keySet()) {
                if ("countMonth".equals(key)) {
                    if (sortMap.get(key)) {
                        comparator = Comparator.comparing(CrawlerReportVO::getCountMonth);
                    } else {
                        comparator = Comparator.comparing(CrawlerReportVO::getCountMonth).reversed();
                    }
                }
                if ("countYear".equals(key)) {
                    if (sortMap.get(key)) {
                        comparator = Comparator.comparing(CrawlerReportVO::getCountYear);
                    } else {
                        comparator = Comparator.comparing(CrawlerReportVO::getCountYear).reversed();
                    }
                }
            }
            tempStream = tempStream.sorted(comparator);
        }
        //内存中分页
        int total = itemList.size();
        int page = dto.getPage() == null ? 1 : dto.getPage();
        int pageSize = dto.getPageSize() == null ? 10 : dto.getPageSize();
        int a = total / pageSize;
        int pages = total % pageSize > 0 ? a + 1 : a;

        itemList = tempStream.peek(s -> s.setChannel(channelMap.get(s.getChannel()))).skip((page - 1) * pageSize).limit(pageSize).collect(Collectors.toList());
        SearchDataResult<CrawlerReportVO> finalRes = new SearchDataResult<>();
        finalRes.setContent(itemList);
        finalRes.setPages(pages);
        finalRes.setTotal(total);
        return finalRes;
    }

    public void syncAccountReport(CrawlerReportDTO dto) {
        List<CrawlerReportVO> res = Lists.newArrayList();
        Map<String, String> channelMap = dicService.getDicByType("channel");
        res.addAll(crawlerStrategyMapper.listReportRed(dto));
        res.addAll(crawlerStrategyMapper.listReportWX(dto));
        res.addAll(crawlerStrategyMapper.listReportTiktok(dto));
        reportService.saveReportBatch(res);

    }

}