package com.sun.content.api.common.utils;

import java.util.List;

/**
 * @author sunshilong
 * @version 1.0
 * @date 2022/3/7
 */
@FunctionalInterface
public interface ListProcessable<V> {

    /**
     * 自定义处理过程
     *
     * @param list
     */
    void process(List<V> list);

}

