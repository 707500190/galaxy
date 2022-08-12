package com.sun.content.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.content.search.AbstractESQueryBuilder;
import com.sun.content.search.SearchOption;
import com.sun.content.service.ElasticSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesRequest;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.sort.SortBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author sunshilong
 * @since 2022-03-07
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class ElasticSearchServiceImpl implements ElasticSearchService {

    private final ObjectMapper objectMapper;

    private final RestHighLevelClient client;

    private final ElasticsearchRestTemplate restTemplate;


    @Override
    public <T> SearchHits<T> doSearch(AbstractESQueryBuilder esQueryBuild, Class<T> clazz) {
        Assert.notNull(esQueryBuild, "search error, search esQueryBuild is null !");

        SearchOption param = esQueryBuild.getSearchOption();
        String index = param.getIndex();
        IndexCoordinates indexName = IndexCoordinates.of(index);

        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder()
                .withQuery(esQueryBuild.getQueryBuilder())
                .withPageable(esQueryBuild.getPageable())
                .withHighlightBuilder(esQueryBuild.getHighlightBuilder());
        //默认返回所有字段
        //.withFields(esQueryBuild.getEsReturnFields().toArray(new String[0]));   //返回的字段

        if (!CollectionUtils.isEmpty(esQueryBuild.getSortBuilders())) {
            for (SortBuilder<?> sortBuilder : esQueryBuild.getSortBuilders()) {
                queryBuilder.withSort(sortBuilder);
            }
        }
        NativeSearchQuery nativeSearchQuery = queryBuilder.build();

        String query = nativeSearchQuery.getQuery().toString().replaceAll("\n", "").replaceAll(" ", "");
        log.info("query: {}\n", query);
        if (nativeSearchQuery.getElasticsearchSorts() != null) {
            log.info("Sort: {}\n",
                    nativeSearchQuery.getElasticsearchSorts().toString().replaceAll("\n", "").replaceAll(" ", ""));
        }
        if (nativeSearchQuery.getHighlightBuilder() != null) {
            log.info("Highlight: {}\n",
                    nativeSearchQuery.getHighlightBuilder().toString().replaceAll("\n", "").replaceAll(" ", ""));
        }
        return restTemplate.search(nativeSearchQuery, clazz, indexName);
    }


    @Override
    public void createIndex(String index) {
        // 检查索引是否存在
        boolean nodeExist = this.isIndexExist(index);
        if (nodeExist) {
            throw new RuntimeException("the index is exists, don't repeat create it !!");
        }
        this.createNewIndex(index);
    }


    @Override
    public void indexDocBulk(String indexName, Collection<?> sourceDtoCollection) {
//        final String threadName = Thread.currentThread().getName();
//
//        BulkRequest bulkRequest = new BulkRequest();
//        bulkRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.NONE);
//        sourceDtoCollection.forEach(source -> {
//            try {
//                String jsonSource = objectMapper.writeValueAsString(source);
//                IndexRequest request = new IndexRequest().index(indexName)
//                        .id(getContentId(jsonSource))
//                        .source(jsonSource, XContentType.JSON);
//                bulkRequest.add(request);
//            } catch (JsonProcessingException e) {
//                log.error(e.getMessage(), e);
//            }
//        });
//        try {
//            BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);
//            if (bulkResponse.hasFailures()) {
//                log.warn("bulk index doc failed,\n thread:{} \n message:{}, \nindex info:{}",
//                        threadName, bulkResponse.buildFailureMessage(), indexName);
//            }
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//        }
    }


    @Override
    public void deleteDocBulk(String indexName, List<String> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            log.info("index info:{}, id list is empty", indexName);
            return;
        }
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
        idList.forEach(x -> bulkRequest.add(new DeleteRequest().index(indexName).id(x)));
        try {
            BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);
            if (bulkResponse.hasFailures()) {
                log.error("bulk delete doc failed, message:{}, indexName:{}",
                        bulkResponse.buildFailureMessage(), indexName);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public <T> void updateDocBulk(String indexName, Collection<T> sourceDtoCollection, boolean shouldUpsertDoc) {

//        if (CollectionUtils.isEmpty(sourceDtoCollection)) {
//            return;
//        }
//        BulkRequest bulkRequest = new BulkRequest();
//        bulkRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
//        sourceDtoCollection.forEach(source -> {
//            try {
//                String jsonSource = objectMapper.writeValueAsString(source);
//                // "detect_noop": false 设置null
//                bulkRequest.add(new UpdateRequest().docAsUpsert(shouldUpsertDoc).index(indexName)
//                        .id(getContentId(jsonSource))
//                        .doc(jsonSource, XContentType.JSON).detectNoop(false));
//            } catch (JsonProcessingException e) {
//                log.warn(e.getMessage(), e);
//            }
//        });
//
//        try {
//            BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);
//            if (bulkResponse.hasFailures()) {
//                log.error("bulk update doc failed, message:{}, indexName:{}",
//                        bulkResponse.buildFailureMessage(), indexName);
//            }
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//        }
    }


    @Override
    public <T> T getDocDetail(String indexName, Class<T> clazz, String id) {
        if (StringUtils.isEmpty(indexName)) {
            log.warn(" index info:{}, id is empty", indexName);
            return null;
        }
        GetRequest getRequest = new GetRequest().index(indexName).id(id);
        try {
            GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
            if (!getResponse.isExists()) {
                return null;
            }
            return objectMapper.readValue(getResponse.getSourceAsBytes(), clazz);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public void addAlias(String indexName, String alias) {
        IndicesAliasesRequest request = new IndicesAliasesRequest();
        IndicesAliasesRequest.AliasActions aliasAction = new IndicesAliasesRequest.AliasActions(
                IndicesAliasesRequest.AliasActions.Type.ADD).index(indexName).alias(alias);
        request.addAliasAction(aliasAction);
        try {
            AcknowledgedResponse indicesAliasesResponse = client.indices()
                    .updateAliases(request, RequestOptions.DEFAULT);
            if (!indicesAliasesResponse.isAcknowledged()) {
                log.warn("add alias unacknowledged, indexName:{}, alias:{}", indexName, alias);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void removeIndex(String indexName) {
        try {
            DeleteIndexRequest request = new DeleteIndexRequest(indexName);
            log.info("待删除索引名{}", indexName);
            AcknowledgedResponse response = client.indices().delete(request, RequestOptions.DEFAULT);
            if (!response.isAcknowledged()) {
                log.warn("delete index unacknowledged, indexName:{}", indexName);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public boolean isIndexExist(String indexName) {
        try {
            GetIndexRequest blueRequest = new GetIndexRequest();
            blueRequest.indices(indexName);
            return client.indices().exists(blueRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            log.warn("index:{} not exist", indexName);
        }
        return false;
    }

    @Override
    public boolean isAliasExists(String indexName) {
        // 历史问题造成索引名字和别名相同
        GetAliasesRequest aliasRequest = new GetAliasesRequest(indexName);
        try {
            return client.indices().existsAlias(aliasRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            log.warn("alias:{} not exist", indexName);
            throw new RuntimeException("isAliasExists method is error");
        }
    }


    private void createNewIndex(String indexName) {
        try {

//            CreateIndexRequest request = new CreateIndexRequest(indexName);
//            String pathName = "es_mapping_ik_smart.json";
//            String settingSource = IOUtils.toString(
//                    Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream(pathName)),
//                    Charset.defaultCharset());
//            request.source(settingSource, XContentType.JSON);
//            CreateIndexResponse response = client.indices().create(request, RequestOptions.DEFAULT);
//
//            boolean acknowledged = response.isAcknowledged();
//            boolean shardsAcknowledged = response.isShardsAcknowledged();
//            if (!acknowledged || !shardsAcknowledged) {
//                log.warn("create index unacknowledged, index info:{}", indexName);
//            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 获取唯一的contentId
     * 规择： contentId = channel + id
     *
     * @param jsonSource
     * @return
     */
    private String getContentId(String jsonSource) {
        ObjectMapper mapper = new ObjectMapper();
        Map readValue = null;
        try {
            readValue = mapper.readValue(jsonSource, Map.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        String id = (String) readValue.get("id");
        return id;
    }
}
