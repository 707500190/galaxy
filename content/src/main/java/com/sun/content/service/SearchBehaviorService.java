package com.sun.content.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sun.content.api.entity.SearchBehavior;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.List;
import java.util.Set;

/**
 * 搜索行为业务类
 *
 * @author sunshilong
 * @version 1.0
 * @date 2022/5/6
 */
public interface SearchBehaviorService extends IService<SearchBehavior> {

    /**
     * 保存搜索关键字的频次（频率优先）；
     *
     * @param keyword 关键字
     */
    void saveKeywordRank(String keyword);

    /**
     * 保存关键字搜索历史；
     *
     * @param keyword 关键字
     * @param type 行为类型
     */
    void saveBehavior(String keyword, String type);

    /**
     * 获取所有关键词排行信息；
     *
     * @return 返回排行信息；
     */
    Set<ZSetOperations.TypedTuple<String>> getKeywordRank();

    /**
     * 获取搜索历史
     *
     * @param keyword 传入的关键字模糊搜索
     * @return 相似关键字
     */
    List<String> getHistoryByKeyword(String keyword);

}
