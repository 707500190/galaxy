package com.sun.content.service;

import com.sun.content.search.AbstractESQueryBuilder;
import org.springframework.data.elasticsearch.core.SearchHits;

import java.util.Collection;
import java.util.List;

/**
 * ES查询类
 *
 * @author sunshilong
 * @version 1.0
 * @date 2022/3/7
 */
public interface ElasticSearchService {



    /**
     * 根据构造的ES查询语句，执行查询；
     *
     * @param <T>  返回数据类型
     * @param esQueryBuild es查询建造类
     * @param clazz 返回数据类型类对象
     * @return
     */
    <T> SearchHits<T> doSearch(AbstractESQueryBuilder esQueryBuild, Class<T> clazz);

    /**
     * 创建索引名
     *
     * @param index 名称
     */
    void createIndex(String index);

    /**
     * 同步数据(支持批量和单条)
     *
     * @param indexName 索引名称
     * @param sourceDtoCollection 同步的数据集合
     */
    @Deprecated
    void indexDocBulk(String indexName, Collection<?> sourceDtoCollection);

    /**
     * 批量更新文档；
     *
     * @param indexName 索引名称
     * @param sourceDtoCollection 待执行文档集合
     * @param shouldUpsertDoc  true文档不存在时，会新增文档；
     * @param <T>
     */
    <T> void updateDocBulk(String indexName, Collection<T> sourceDtoCollection, boolean shouldUpsertDoc);

    /**
     * 删除文档（支持批量/单条）
     *
     * @param indexName 索引名称
     * @param idList   注意！！ 这里是es主键值集合 _id
     */
    void deleteDocBulk(String indexName, List<String> idList);

    /**
     * 根据esID 获取一条文档
     *
     * @param <T>
     * @param indexName
     * @param clazz
     * @param id es id
     * @return
     */
    <T> T getDocDetail(String indexName, Class<T> clazz,  String id);

    /**
     * 添加别名
     *
     * @param indexName 索引名称
     * @param alias  别名
     */
    void addAlias(String indexName, String alias);

    void removeIndex(String indexName);

    /**
     * 当前的索引是否存在
     *
     * @param indexName  索引名称
     * @return
     */
    boolean isIndexExist(String indexName);


    boolean isAliasExists(String indexName);


}
