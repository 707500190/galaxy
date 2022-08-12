package com.sun.content.service;

import com.sun.content.api.vo.ContentInfoVO;
import com.sun.content.api.entity.ContentInfo;
import com.sun.content.api.vo.SearchDataResult;
import com.sun.content.search.SearchOption;

/**
 * @author sunshilong
 * @version 1.0
 * @date 2022/3/7
 */
public interface ContentSearchService {
    /**
     * 分页查询
     *
     * @param searchOption 查询参数
     * @return 聚合的结果
     */
     SearchDataResult<ContentInfoVO> page(SearchOption searchOption);

    /**
     * ES id详情查询；
     *
     *
     * @param IndexName 索引名称
     * @param id es主键
     * @param keyword
     * @return ES 文档
     */
    ContentInfo getDetailById(String IndexName, String id, String keyword);
}
