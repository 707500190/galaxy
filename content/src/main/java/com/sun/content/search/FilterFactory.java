package com.sun.content.search;

import com.sun.content.api.dto.Pair;
import com.sun.content.api.common.constant.ElasticConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.*;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.Objects;

import static com.sun.content.api.common.constant.ElasticConstants.ID_FIELD;

/**
 * 构造Filter查询
 *
 * @author sunshilong
 * @version 1.0
 * @date 2022/3/7
 */
@Slf4j
public class FilterFactory {

    public static QueryBuilder createFilter(SearchOption searchOption){
        Assert.notNull(searchOption, "searchOption is null!");

        BoolQueryBuilder finalFilter = QueryBuilders.boolQuery();
        SearchOption.Filter filterOption = searchOption.getFilter();
        if (filterOption == null) {
            return finalFilter;
        }
        //发布时间
        Pair<Date> region ;
        if (Objects.nonNull(region = filterOption.getPublishTime())) {
            RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(ElasticConstants.PUBLISH_TIME_FIELD);
            if (region.getStart() != null) {
                rangeQueryBuilder.gte(region.getStart().getTime());
            }
            if (region.getEnd() != null) {
                rangeQueryBuilder.lte(region.getEnd().getTime());
            }
            finalFilter.must(rangeQueryBuilder);
        }

        //渠道
        if (StringUtils.isNotEmpty(filterOption.getChannel())) {
            TermQueryBuilder term = QueryBuilders.termQuery(ElasticConstants.CHANNEL_FIELD, filterOption.getChannel());
            finalFilter.must(term);
        }

        //粉丝数量
        Pair<Integer> fans;
        if (Objects.nonNull(fans = filterOption.getFansNumber())) {
            RangeQueryBuilder range = QueryBuilders.rangeQuery(ElasticConstants.FANS_NUMBER);
            if (fans.getStart() != null) {
                range.gte(fans.getStart());
            }
            if (fans.getEnd() != null) {
                range.lte(fans.getEnd());
            }
            finalFilter.must(range);
        }
        if (!StringUtils.isEmpty(filterOption.getId())) {
            TermQueryBuilder term = QueryBuilders.termQuery(ID_FIELD, filterOption.getId());
            finalFilter.must(term);
        }
        return finalFilter;
    }


}
