package com.sun.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.sun.content.api.dto.CrawlerBaseDTO;
import com.sun.content.api.entity.CrawlerStrategy;
import com.sun.content.api.entity.CrawlerStrategyAccount;
import com.sun.content.exception.CheckedException;
import com.sun.content.mapper.CrawlerStrategyMapper;
import com.sun.content.service.CrawlerStrategyAccountService;
import com.sun.content.service.CrawlerStrategyService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * @描述： 服务类
 * @作者: sunshilong
 * @日期: 2022-03-14
 */
@Service
@RequiredArgsConstructor
public class CrawlerStrategyServiceImpl extends ServiceImpl<CrawlerStrategyMapper, CrawlerStrategy> implements CrawlerStrategyService {

    private final CrawlerStrategyAccountService accountService;


    @Override
    public void saveStrategyAndAccounts(CrawlerBaseDTO dto) {

        //查询策略,不存在则保存策略；
        CrawlerStrategy valid = this.getOne(new QueryWrapper<CrawlerStrategy>().eq("channel", dto.getChannel()));
        if (Objects.nonNull(valid)) {
            dto.setId(valid.getId());
        } else {
            Date now = new Date();
            CrawlerStrategy crawlerStrategy = new CrawlerStrategy();
            BeanUtils.copyProperties(dto, crawlerStrategy);
            crawlerStrategy.setUpdateTime(now);
            crawlerStrategy.setCreateTime(now);
            this.save(crawlerStrategy);
            dto.setId(crawlerStrategy.getId());
        }
        //format check
        QueryWrapper<CrawlerStrategyAccount> wrapper = new QueryWrapper<>();
        wrapper.eq("strategy_id", dto.getId());
        List<CrawlerStrategyAccount> accountInfoList = this.accountService.list(wrapper);
        List<String> accountDBList = accountInfoList.stream().map(CrawlerStrategyAccount::getAccount).collect(Collectors.toList());
        List<String> newAccounts = getAccounts(dto.getAccount());
        //数据库重复校验
        List<String> repeatAccount = Lists.newArrayList();
        newAccounts.stream().peek(s -> {
            if (accountDBList.contains(s)) repeatAccount.add(s);
        }).collect(Collectors.toList());
        if (repeatAccount.size() > 0) {
            throw new CheckedException(2001, "存在重复的账号：" + repeatAccount.toString());
        }
        accountService.saveAccountBatch(newAccounts, dto);

    }

    @Override
    public void updateStrategyAndAccounts(CrawlerBaseDTO dto) {

        Long strategyId = dto.getId();
        if (Objects.isNull(strategyId) && StringUtils.isEmpty(dto.getChannel())) {
//            throw new CheckedException(MISS_ID_CHANNEL);
        }
        //暂时不对策略更新
//        CrawlerStrategy strategy = getById(strategyId);
//        strategy.setChannel(dto.getChannel());
//        this.updateById(strategy);
        List<String> newAccounts = getAccounts(dto.getAccount());
        accountService.saveAccountBatchAndRemove(newAccounts, dto);
    }

    @Override
    public CrawlerBaseDTO getStrategyById(Long id) {
        CrawlerStrategy strategy = this.getById(id);
        CrawlerBaseDTO dto = new CrawlerBaseDTO();
        if (Objects.isNull(strategy)) {
            return dto;
        }
        BeanUtils.copyProperties(strategy, dto);
        List<String> accounts = accountService.list(new QueryWrapper<CrawlerStrategyAccount>().eq("strategy_id", id))
                .stream()
                .filter(Objects::nonNull)
                .map(CrawlerStrategyAccount::getAccount)
                .collect(Collectors.toList());
        String finalAccounts = formatAccount(accounts);
        dto.setAccount(finalAccounts);
        return dto;
    }


    private List<String> getAccounts(String account) {
        if (StringUtils.isEmpty(account)) {
            return Lists.newArrayList();
        }
        account = account.replaceAll(",", "，");
        account = account.replaceAll(" ", "");
        String[] list = account.split("，");

        List<String> res = Lists.newArrayList(list);
        List<String> repeat = checkRepeat(Lists.newArrayList(list));
        if (repeat.size() > 0) {
            throw new CheckedException(2001, "存在重复的账号：" + repeat);
        }
        return res;
    }

    public static <T> List<T> checkRepeat(List<T> list) {
        return list.stream()
                .collect(Collectors.toMap(e -> e, e -> 1, Integer::sum)) // 获得元素出现频率的 Map，键为元素，值为元素出现的次数
                .entrySet().stream() // Set<Entry>转换为Stream<Entry>
                .filter(entry -> entry.getValue() > 1) // 过滤出元素出现次数大于 1 的 entry
                .map(Map.Entry::getKey) // 获得 entry 的键（重复元素）对应的 Stream
                .collect(Collectors.toList()); // 转化为 List
    }

    private String formatAccount(List<String> account) {
        if (CollectionUtils.isEmpty(account)) {
            return "";
        }
        return StringUtils.join(account, "，");
    }

}