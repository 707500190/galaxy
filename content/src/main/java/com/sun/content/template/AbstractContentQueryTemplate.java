package com.sun.content.template;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.google.common.collect.Lists;
import com.sun.content.api.common.constant.ElasticConstants;
import com.sun.content.api.common.utils.ESUtils;
import com.sun.content.api.dto.QueryContentDTO;
import com.sun.content.api.entity.EsSyncable;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 每个渠道表同步查询父类
 *
 * @param <T>
 * @author sunshilong
 */

@Slf4j
public abstract class AbstractContentQueryTemplate<T extends EsSyncable> implements ContentTemplateService<T> {


    /**
     * 查询数据
     */
    @Override
    public List<QueryContentDTO> getContentList(Wrapper<T> wrapper) {
        if (Objects.isNull(wrapper)) {
            wrapper = getWrapper();
        }
        IService<T> service = getQueryService();
        if (Objects.nonNull(service)) {
            List<T> entityList = service.list(wrapper);
            return handle(entityList);
        } else {
            log.error("The query's service is not injected");
            return Lists.newArrayList();
        }
    }

    /**
     * 默认查询条件！  now() -1天 < insert_date < now()
     * (防止数据量过大时无条件全表查询崩溃，考虑分批查询条件外置)
     * 可根据不同渠道的表覆盖对应的查询同步逻辑；
     *
     * @return Wrapper 查询条件；
     */
    Wrapper<T> getWrapper() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        Date start = cal.getTime();
        Date end = new Date();
        QueryWrapper<T> wrapper = new QueryWrapper<>();
        wrapper.ge(ElasticConstants.INSERT_DATE, start);
        wrapper.le(ElasticConstants.INSERT_DATE, end);
        return wrapper;
    }

    protected abstract IService<T> getQueryService();

    protected abstract Class<T> getType();

    /**
     * 所有模板统一处理结果；
     *
     * @param entity 数据库查询的实体；
     * @return 结果集
     */
    @Override
    public List<QueryContentDTO> handle(List<T> entity) {
        return entity.stream().filter(Objects::nonNull).map(s -> {
            QueryContentDTO res = new QueryContentDTO();
            BeanUtils.copyProperties(s, res);
            res.setDbId(s.getId());
            try {
                res.setId(ESUtils.getESId(s));
            }catch (Exception e){
                //TODO:之后添加同步日志表，记录失败任务；
                log.error("sync error, because of getEsId method, channel:{}, dbId:{}", s.getChannel(), s.getId());
                e.printStackTrace();
            }
            //自定义处理res
            parse(s, res);
            return res;
        }).filter(s -> StringUtils.isNotEmpty(s.getId())).collect(Collectors.toList());
    }

    /**
     * 个性化处理结果扩展此方法；
     *
     * @param e 查询的实体；
     */
    void parse(T e, QueryContentDTO res) {

    }

}