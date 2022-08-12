package com.sun.content.search;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.sun.content.api.common.constant.ElasticConstants;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;

import java.util.*;

import static org.elasticsearch.index.query.MultiMatchQueryBuilder.Type.PHRASE;

/**
 * 抽象的搜索建造类
 *
 * @author sunshilong
 * @date 2022/3/7
 */
@Getter
@Slf4j
public abstract class AbstractESQueryBuilder {

    protected SearchOption searchOption;
    protected QueryBuilder queryBuilder;
    protected List<SortBuilder<?>> sortBuilders = new ArrayList<>();
    protected Pageable pageable;
    protected HighlightBuilder highlightBuilder;
    protected Set<String> esReturnFields = new HashSet<>();
    /**
     * true: 不管返回多少条数据, 都会搜索全部符合条件的数据, 返回结果总量(totalHits)==实际命中数量, 适合需要返回精确总量的查询
     * false: 不管返回多少条数据, 最多搜索10000条符合条件的数据, 返回结果总量(totalHits)<=10000, 适合下拉列表(联想)
     */
    protected Boolean trackTotalHits;


    /**
     * 所有搜索场景都会返回的字段,一些基本的字段(目前暂未使用，调用接口处已注释！！！)
     */
    private static final Set<String> COMMON_RETURN_FIELDS = new ImmutableSet.Builder<String>()
            .add(ElasticConstants.CHANNEL_FIELD)
            .add(ElasticConstants.TITLE_FIELD)
            .add(ElasticConstants.PUBLISH_TIME_FIELD)
            .build();

    protected AbstractESQueryBuilder(SearchOption searchOption) {
        this.searchOption = searchOption;
        this.trackTotalHits = true;
        setQueryBuilder();
        setSortBuilder();
        setHighlightBuilder();
        setPageable();
        setEsReturnFields();
    }

    protected void setPageable() {
        // es page is zero-based
        int page = searchOption.getPage() - 1;
        int pageSize = searchOption.getPageSize();
        this.pageable = PageRequest.of(page, pageSize);
    }

    /**
     * 给与逻辑语句显式地添加括号，保证搜索逻辑不会随词语位置发生变化，参考：
     * http://elasticsearch-users.115913.n3.nabble.com/Query-string-operators-seem-to-not-be-working-correctly-td4055615.html
     * https://lucene.apache.org/core/5_4_1/queryparser/org/apache/lucene/queryparser/classic/package-summary.html
     *
     * @param keywords 用户输入关键词
     * @return 添加优先级的搜索条件
     */
    protected static String parenthesize(String keywords) {
        // 空串是特定的搜索条件，直接返回即可, 详见SearchOption.parseKeyword()
        if (" ".equals(keywords)) {
            return keywords;
        }
        ArrayList<String> res = new ArrayList<>();
        for (String ANDLogic : keywords.split("\\+")) {
            if (!StringUtils.isEmpty(ANDLogic)) {
                res.add(String.format("(%s)", ANDLogic.replaceAll(" ", " AND ")));
            }
        }
        return StringUtils.join(res, " OR ");
    }

    /**
     * 可搜索的文本字段的权重，es默认的权重是2.2而不是1.0，
     */
    Map<String, Float> FIELD_BOOST_MAP = new ImmutableMap.Builder<String, Float>()
            .put(ElasticConstants.TITLE_FIELD, 10.0F)
            .put(ElasticConstants.ACCOUNT_TAG, 9.9F)
            .put(ElasticConstants.ARTICLE_CONTENT_FIELD, 9.9F)
            .build();
    /**
     * 使用query_string的方式进行关键字查询
     *
     * @return QueryBuilder
     */
    protected QueryBuilder queryBuilderWithQueryString(String keyword, MultiMatchQueryBuilder.Type type) {
        String queryString = parenthesize(keyword);
        QueryStringQueryBuilder queryStringQueryBuilder = QueryBuilders.queryStringQuery(queryString);
        HashMap<String, Float> boostMap = new HashMap<>();
        //BOOST 权重设置， 后续如果有打分权重计算的需求此处添加
        for (String searchField : initSearchFields()) {
            boostMap.put(searchField, FIELD_BOOST_MAP.getOrDefault(searchField, 2.2F));
        }
        queryStringQueryBuilder.defaultOperator(Operator.AND);
        queryStringQueryBuilder.type(type);
        queryStringQueryBuilder.phraseSlop(0);
        //全文搜索的哪些字段
        queryStringQueryBuilder.fields(boostMap);
        return queryStringQueryBuilder;
    }


    /**
     * 使用FunctionScoreQuery构造关键词相关的query builder。
     * 此模式下，最终的排序分（_score）由两大部分合并而成：原始_score和附加权重weight,合并的方式可以是加和，相乘，最大，最小等，
     * 其中，weight是由一组加权函数的值合并而成，加权函数可以是简单的一个数值，也可以根据某个字段的值计算得到。
     * <p>
     * https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl-function-score-query.html#score-functions
     *
     * @return QueryBuilder
     */
    protected QueryBuilder getQueryStringBuilder() {
        QueryBuilder originQueryBuilder;
        if (!StringUtils.isEmpty(searchOption.getKeyword())) {
            // 有关键字时，使用queryString，原始_score为关键字的相关性得分，后续与权重weight相乘得到最终得分
            originQueryBuilder = queryBuilderWithQueryString(searchOption.getKeyword(), this.keywordMultiMatchType());
        } else {
            // 无关键字时，使用match_all，原始_score是常量1.0，后续与权重weight相乘得到最终得分
            originQueryBuilder = QueryBuilders.matchAllQuery();
        }
        // 后续此处添加权重_score 排序打分策略
        return originQueryBuilder;
    }

    void setQueryBuilder() {
        BoolQueryBuilder finalQuery = QueryBuilders.boolQuery();
        // 后续可使用function score控制文档得分，用_score进行排序
        QueryBuilder queryStringQueryBuilder = getQueryStringBuilder();
        finalQuery.must(queryStringQueryBuilder);
        QueryBuilder filter = FilterFactory.createFilter(searchOption);
        finalQuery.filter(filter);
        this.queryBuilder = finalQuery;
    }

    /**
     * 关键词匹配的类型，除人名搜索（PHRASE）外，都是用BEST_FIELDS方式
     *
     * @return  匹配类型
     */
    protected MultiMatchQueryBuilder.Type keywordMultiMatchType() {
        return PHRASE;
    }

    //设置高亮
    void setHighlightBuilder() {
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        for (String searchField : initHighlightFields()) {
            if (ElasticConstants.ARTICLE_CONTENT_FIELD.equals(searchField)) {
                highlightBuilder.field(searchField,150).noMatchSize(150);
            }else {
                highlightBuilder.field(searchField,150,1);
            }
        }
        this.highlightBuilder = highlightBuilder;

    }

    void setEsReturnFields() {
        this.esReturnFields.addAll(initReturnFields());
        this.esReturnFields.addAll(COMMON_RETURN_FIELDS);
    }

    void setSortBuilder() {
        if (CollectionUtils.isEmpty(searchOption.getSort())) {
            this.sortBuilders = initSortBuilder();
            return;
        }
        Map<String, Boolean> sortBy = searchOption.getSort();
        for (String key : sortBy.keySet()) {
            if (StringUtils.isNotEmpty(key)) {
                if (sortBy.get(key)) {
                   this.sortBuilders.add(SortBuilders.fieldSort(key).order(SortOrder.ASC));
                } else {
                    this.sortBuilders.add(SortBuilders.fieldSort(key).order(SortOrder.DESC));
                }
            }
        }
    }

    /**
     * 默认排序的规则
     *
     * @return 排序条件
     */
    abstract List<SortBuilder<?>> initSortBuilder();

    /**
     * es单次搜索应该搜索哪些字段。这些字段理论上应该都是text或keyword类型（es中）
     *
     * @return SearchFields
     */
    abstract Set<String> initSearchFields();

    /**
     * es单次搜索应该返回哪些字段。各种类型的字段都可以返回。
     * 注意：这些字段应该和initHighlightFields()方法返回值互斥，因为es高亮的字段肯定会返回（searchHit.getHighlightFields()）
     *
     * @return ReturnFields
     */
     Set<String> initReturnFields() {
        return new HashSet<>();
    }

    /**
     * es单次搜索因该高亮哪些字段。这些字段理论上应该都是text或keyword类型（es中）
     * 注意：这些字段应该和initReturnFields()方法返回值互斥，因为es高亮的字段肯定会返回（searchHit.getHighlightFields()）
     *
     * @return HighlightFields
     */
    abstract Set<String> initHighlightFields();
}
