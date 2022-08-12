package com.sun.content.search;

import com.google.common.collect.ImmutableSet;
import com.sun.content.api.common.constant.ElasticConstants;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.sun.content.api.common.constant.ElasticConstants.ARTICLE_LIKE_NUMBER_FIELD;
import static com.sun.content.api.common.constant.ElasticConstants.PUBLISH_TIME_FIELD;
import static org.elasticsearch.index.query.MultiMatchQueryBuilder.Type.PHRASE;

/**
 * 默认ESQuery建造类
 *
 * @author sunshilong
 * @version 1.0
 * @date 2022/3/7
 */
public class DefaultESQueryBuilder extends AbstractESQueryBuilder {

    public DefaultESQueryBuilder(SearchOption searchOption) {
        super(searchOption);
    }

    @Override
    Set<String> initSearchFields() {
        ImmutableSet<String> searchFields = new ImmutableSet.Builder<String>()
                .add(ElasticConstants.TITLE_FIELD)
                .add(ElasticConstants.ACCOUNT_TAG)
                .add(ElasticConstants.ARTICLE_CONTENT_FIELD)
                .build();
        return searchFields;
    }


    @Override
    Set<String> initHighlightFields() {
        return initSearchFields();
    }

    @Override
    protected List<SortBuilder<?>> initSortBuilder() {
        List<SortBuilder<?>> sortBuilders = new ArrayList<>();
        //默认为点赞数倒序排序
        sortBuilders.add(SortBuilders.fieldSort(ARTICLE_LIKE_NUMBER_FIELD).order(SortOrder.DESC));
        sortBuilders.add(SortBuilders.fieldSort(PUBLISH_TIME_FIELD).order(SortOrder.DESC));
        return sortBuilders;
    }

    protected MultiMatchQueryBuilder.Type keywordMultiMatchType() {
        return PHRASE;
    }
}
