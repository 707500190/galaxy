package com.sun.content.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sun.content.api.dto.QueryContentDTO;
import com.sun.content.api.entity.EsSyncable;
import com.sun.content.api.entity.ContentInfo;

import java.util.Date;
import java.util.List;
import java.util.function.Supplier;

/**
 * 内容同步类
 *
 * @author sunshilong
 * @version 1.0
 * @date 2022/3/7
 */
public interface ContentSyncService {

    /**
     * 查询的Mysql全渠道表数据
     *
     *
     * @param supplier 模板查询规则 ：null——> 默认查询条件  now() -1天 < insert_date < now()
     * @param tempKeyList 指定模板key  null——> 默认查询所有模板
     */
    List<QueryContentDTO> batchQueryByTemplatesAndSupplier(Supplier<QueryWrapper<EsSyncable>> supplier, List<String> tempKeyList);

    /**
     * 同步 insert_date 在参数"start"，"end"范围内的一批数据；
     *
     *  @param indexName  索引名称
     * @param start  insert_date 开始时间
     * @param end  insert_date 结束时间
     * @param size   每次向ES同步的数量   建议 5000左右（建议一次同步es大小为5 - 15M）
     * @param keyList  需要同步的渠道维度  1 小红书 2微信 3 抖音  null默认全部
     */
    void syncDbToESByDateAndKeys(String indexName, Date start, Date end, Integer size, List<String> keyList);


    /**
     * 根据一批Ids和指定的模板key进行同步
     * （可选择的模板维度同步）
     *
     *  @param ids  id
     * @param indexName 索引名称
     * @param keyList 同步哪几张表
     * @param size 一次向ES同步多少数据
     */
    void syncDbToESByIdsAndKeys(List<Long> ids, String indexName, List<String> keyList, int size);

    /**
     * 对Mysql查出的数据进行组装
     *
     * @param result  mysql查询的结果集
     * @return SearchContentInfo
     */
    List<ContentInfo> installESResult(List<QueryContentDTO> result);

    /**
     * 根据账号更新标签；
     *  @param account 一批账号集合
     * @param tags 标签
     */
    void updateESTagByAccount(List<String> account, String tags);



}
