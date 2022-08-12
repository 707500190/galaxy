package com.sun.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.sun.content.api.common.constant.ElasticConstants;
import com.sun.content.api.common.utils.ProcessByStep;
import com.sun.content.api.dto.QueryContentDTO;
import com.sun.content.api.entity.ContentInfo;
import com.sun.content.api.entity.EsSyncable;
import com.sun.content.service.ContentSyncService;
import com.sun.content.service.CrawlerStrategyAccountService;
import com.sun.content.template.AbstractContentQueryTemplate;
import com.sun.content.template.QueryDataFromDBCallable;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * 内容同步类
 *
 * @author sunshilong
 * @version 1.0
 * @date 2022/3/7
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Data
public class ContentSyncServiceImpl implements ContentSyncService {


    @Qualifier("queryThreadPoolExecutor")
    private final ThreadPoolExecutor pool;

    private final ElasticsearchRestTemplate restTemplate;

    private final CrawlerStrategyAccountService accountService;

    //TODO: 不知道为啥  Map<String, AbstractContentQueryTemplate<EsSyncable>> templateMap;
    // 这里指定泛型之后就会注入失败；
    @Resource
    private Map<String, AbstractContentQueryTemplate> templateMap;


    @Override
    @SuppressWarnings("unchecked")
    public List<QueryContentDTO> batchQueryByTemplatesAndSupplier(Supplier<QueryWrapper<EsSyncable>> supplier, List<String> tempKeyList) {
        List<QueryContentDTO> resultList = Lists.newArrayList();
        //获取任务并执行；
        List<Callable<List<QueryContentDTO>>> calls = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(templateMap)) {
            //不指定模板则查询所有模板
            if (CollectionUtils.isEmpty(tempKeyList)) {
                for (AbstractContentQueryTemplate<EsSyncable> s : templateMap.values()) {
                    calls.add(new QueryDataFromDBCallable<>(s, supplier.get()));
                }
            } else {
                //获取指定的查询模板
                for (String templateKey : tempKeyList) {
                    AbstractContentQueryTemplate<EsSyncable> template = templateMap.get(templateKey);
                    Callable<List<QueryContentDTO>> call = new QueryDataFromDBCallable<>(template, supplier.get());
                    calls.add(call);
                }
            }
        }
        //获取 查询返回结果；
        List<Future<List<QueryContentDTO>>> futures;
        try {
            futures = pool.invokeAll(calls);
            for (Future<List<QueryContentDTO>> f : futures) {
                List<QueryContentDTO> queryResult = f.get();
                resultList.addAll(queryResult);
            }
        } catch (InterruptedException e) {
            log.error(" query InterruptedException：{}, stack info:[{}]", e.getMessage(),
                    e.getStackTrace());
        } catch (ExecutionException e1) {
            log.error(" query ExecutionException：{}, stack info:[{}]", e1.getMessage(),
                    e1.getStackTrace());
            e1.printStackTrace();
        }
        return resultList;
    }


    public void syncDbToESByIdsAndKeys(List<Long> ids, String indexName, List<String> keyList, int size) {
        //根据ids查询一批数据
        Supplier<QueryWrapper<EsSyncable>> supplier = () -> {
            QueryWrapper<EsSyncable> condition = new QueryWrapper<>();
            if (Objects.nonNull(ids)) {
                condition.in("id", ids);
            }
            return condition;
        };
        batchQueryAndSync(supplier, indexName, keyList, size);
    }

    @Override
    public void syncDbToESByDateAndKeys(String indexName, Date start, Date end, Integer size, List<String> keyList) {
        //根据起始时间和结束时间构造查询；
        Supplier<QueryWrapper<EsSyncable>> supplier = () -> {
            QueryWrapper<EsSyncable> condition = new QueryWrapper<>();
            if (Objects.nonNull(start)) {
                condition.gt(ElasticConstants.INSERT_DATE, start);
            }
            if (Objects.nonNull(end)) {
                condition.lt(ElasticConstants.INSERT_DATE, end);
            }
            return condition;
        };
        batchQueryAndSync(supplier, indexName, keyList, size);
    }

    private void batchQueryAndSync(Supplier<QueryWrapper<EsSyncable>> supplier, String indexName, List<String> templateKeyList, int size) {
        List<QueryContentDTO> originList = batchQueryByTemplatesAndSupplier(supplier, templateKeyList);

        List<ContentInfo> list = installESResult(originList);
        //ES bulk 请求一次建议大小为 5M - 15M的数据量 我们按1000条一次进行bulk同步
        ProcessByStep<ContentInfo> process = new ProcessByStep<>(list, size);
        IndexCoordinates index = IndexCoordinates.of(indexName);
        process.execute((s) -> restTemplate.save(s, index));
        log.info("-----------同步完成！---------------");
    }

    /**
     * 所有渠道的数据汇总进行统一处理
     * 1.对内容进行标签同步（查询内容的账号对应到本地账号表中的标签，进行同步；）
     *
     * @param result mysql查询的结果集
     * @return 重组后的ES对象集合；
     */
    public List<ContentInfo> installESResult(List<QueryContentDTO> result) {

        if (CollectionUtils.isEmpty(result)) return Lists.newArrayList();

        Set<String> accountList = result.stream().map(QueryContentDTO::getAccount).collect(Collectors.toSet());
        Map<String, String> accountTagMap = accountService.getAccountTagMap(accountList);
        log.info("---------内容数据同步标签同步------------");
        return result.stream().map(s -> {
            ContentInfo info = new ContentInfo();
            BeanUtils.copyProperties(s, info);
            if (Objects.nonNull(info.getAccount()) && !CollectionUtils.isEmpty(accountTagMap)) {
                info.setAccountTag(accountTagMap.get(info.getAccount()));
            }
            return info;
        }).collect(Collectors.toList());
    }

    /**
     * 根据Account更新标签
     */
    @Override
    public void updateESTagByAccount(List<String> accountList, String tags) {
        //更新ES
        BoolQueryBuilder boolMust = QueryBuilders.boolQuery();
        for (String acc : accountList) {
            boolMust.must(QueryBuilders.termQuery("account", acc));
        }
        log.info("updateESTagByAccount's query is : query: {}\n", boolMust.toString());
        Query query = new NativeSearchQueryBuilder()
                .withQuery(boolMust).build();
        SearchHits<ContentInfo> hits = restTemplate.search(query, ContentInfo.class);
        //update tags
        List<ContentInfo> list = hits.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList());
        list = list.stream().peek(s -> s.setAccountTag((tags))).collect(Collectors.toList());
        restTemplate.save(list);
    }
}
