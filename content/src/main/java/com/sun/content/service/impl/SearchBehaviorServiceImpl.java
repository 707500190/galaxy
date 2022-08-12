package com.sun.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.content.api.entity.SearchBehavior;
import com.sun.content.mapper.SearchBehaviorMapper;
import com.sun.content.service.SearchBehaviorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 搜索行为
 *
 * @author sunshilong
 * @version 1.0
 * @date 2022/5/6
 */
@Service
@RequiredArgsConstructor
public class SearchBehaviorServiceImpl extends ServiceImpl<SearchBehaviorMapper, SearchBehavior> implements SearchBehaviorService {

    private final static String KEYWORD_RANK_KEY = "keyword:rank";

    private final StringRedisTemplate redisTemplate;


    @Override
    public void saveKeywordRank(String keyword) {
        Boolean key = redisTemplate.hasKey(KEYWORD_RANK_KEY);
        if (null != key && key) {
            redisTemplate.opsForZSet().incrementScore(KEYWORD_RANK_KEY, keyword, 1.0);
        }else {
            redisTemplate.opsForZSet().incrementScore(KEYWORD_RANK_KEY, keyword, 1.0);
            redisTemplate.expire(KEYWORD_RANK_KEY,90 , TimeUnit.DAYS);
        }

    }

    @Override
    public void saveBehavior(String keyword, String type) {
//        this.baseMapper.saveOrUpdateHistory(keyword, type);
    }

    /**
     * 获取热门关键字排行；
     *
     * @return 热门历史前10条
     */
    @Override
    public Set<ZSetOperations.TypedTuple<String>> getKeywordRank() {
        //Set<value,score>
        Set<ZSetOperations.TypedTuple<String>> rank = redisTemplate.opsForZSet().reverseRangeWithScores(KEYWORD_RANK_KEY, 0, 9);
        return  rank;
    }

    @Override
    public List<String> getHistoryByKeyword(String keyword) {
        QueryWrapper<SearchBehavior> wrapper = new QueryWrapper<>();
        wrapper.likeRight("value", keyword);
        return list(wrapper).stream().map(SearchBehavior::getValue).collect(Collectors.toList());
    }
}
