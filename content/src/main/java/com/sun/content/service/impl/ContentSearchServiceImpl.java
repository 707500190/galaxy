package com.sun.content.service.impl;

import com.google.common.collect.Lists;
import com.sun.content.api.entity.ContentInfo;
import com.sun.content.api.vo.ContentInfoVO;
import com.sun.content.api.vo.SearchDataResult;
import com.sun.content.search.AbstractESQueryBuilder;
import com.sun.content.search.DefaultESQueryBuilder;
import com.sun.content.search.SearchOption;
import com.sun.content.service.ContentSearchService;
import com.sun.content.service.ElasticSearchService;
import com.sun.content.service.SearchBehaviorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import sun.plugin.javascript.ReflectUtil;

import java.util.List;
import java.util.Map;

/**
 * @author sunshilong
 * @version 1.0
 * @date 2022/3/7
 */

@Service
@Slf4j
@RequiredArgsConstructor
public class ContentSearchServiceImpl implements ContentSearchService {

    private final ElasticSearchService searchService;

    private final SearchBehaviorService searchBehaviorService;

    @Override
    public SearchDataResult<ContentInfoVO> page(SearchOption searchOption) {
        AbstractESQueryBuilder build = new DefaultESQueryBuilder(searchOption);
        SearchHits<ContentInfoVO> hits = searchService.doSearch(build, ContentInfoVO.class);
        SearchDataResult<ContentInfoVO> res = assembleResult(hits, searchOption.getPageSize());
        //保存用户行为数据；
        searchBehaviorService.saveKeywordRank(searchOption.getKeyword());
        searchBehaviorService.saveBehavior(searchOption.getKeyword(), "history");
        return res;
    }


    /**
     * 组装返回结果
     *
     * @param hits     返回hits结果
     * @param pageSize 页数量
     * @return 查询的结果
     */
    public static SearchDataResult<ContentInfoVO> assembleResult(SearchHits<ContentInfoVO> hits, Integer pageSize) {
        SearchDataResult<ContentInfoVO> res = new SearchDataResult<>();
        List<ContentInfoVO> contentList = Lists.newArrayList();
        for (SearchHit<ContentInfoVO> hit : hits) {
            Map<String, List<String>> highMap = hit.getHighlightFields();
            ContentInfoVO info = hit.getContent();
            if (!CollectionUtils.isEmpty(highMap)) {
                //遍历高亮的字段
                for (String name : highMap.keySet()) {
                    List<String> values = highMap.get(name);
                    if (!CollectionUtils.isEmpty(values)) {
                        //文章详情的
                        switch (name) {
                            case "articleContent":
//                                ReflectUtil.setFieldValue(info, "articleContentList", values);
                                break;
                            default:
//                                ReflectUtil.setFieldValue(info, name, values.get(0));
                        }
                    }
                }
            }
            contentList.add(hit.getContent());
        }
        Integer total = (int) hits.getTotalHits();
        res.setContent(contentList);
        res.setTotal(total);
        res.setPages(total % pageSize == 0 ? total / pageSize : total / pageSize + 1);
        return res;
    }


    @Override
    public ContentInfo getDetailById(String IndexName, String id, String keyword) {
        SearchOption option = new SearchOption();
        option.setIndex(IndexName);
        option.setKeyword(keyword);
        SearchOption.Filter filter = new SearchOption.Filter();
        filter.setId(id);
        option.setFilter(filter);
        SearchDataResult<ContentInfoVO> res = this.page(option);
        List<ContentInfoVO> list = res.getContent();
        if (!CollectionUtils.isEmpty(list)) {
            return list.get(0);
        }
        return new ContentInfo();
    }

    /**
     * 截取文字
     *
     * @param origin 源文本
     * @param len    前后截取的长度
     * @return 结果
     */
    private String subArticle(String origin, String target, int len) {
        int originLength = origin.length();
        int start = StringUtils.indexOf(origin, target);
        int end = target.length() + start;
        int finalStart, finalEnd;
        if (start <= len) {
            finalStart = 0;
        } else {
            finalStart = start - len;
        }
        if (originLength - end >= len) {
            finalEnd = end + len;
        } else {
            finalEnd = originLength;
        }
        String finalArticle = origin.substring(finalStart, finalEnd);
        log.info("finalArticle is {}", finalArticle);
        return finalArticle;
    }

}
