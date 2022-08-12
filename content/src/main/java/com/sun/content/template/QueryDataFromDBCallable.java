package com.sun.content.template;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.sun.content.api.entity.EsSyncable;
import com.sun.content.api.dto.QueryContentDTO;

import java.util.List;
import java.util.concurrent.Callable;


/**
 * DB多线程查询，抽象父类
 *
 * @author sunshilong
 * @version 1.0
 * @date 2022/3/7
 */
public class QueryDataFromDBCallable<T extends EsSyncable> implements Callable<List<QueryContentDTO>> {

    private AbstractContentQueryTemplate<T> template;
    /**
     * 将wrapper传入，可根据传入的条件进行查询；
     * 如果为null则使用默认的查询条件
     */
    private Wrapper<T> wrapper;

    public QueryDataFromDBCallable(AbstractContentQueryTemplate<T> template) {
        this(template, null);
    }

    public QueryDataFromDBCallable(AbstractContentQueryTemplate<T> template, Wrapper<T> wrapper) {
        this.template = template;
        this.wrapper = wrapper;
    }

    @Override
    public List<QueryContentDTO> call() {
        return template.getContentList(wrapper);
    }
}
