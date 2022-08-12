package com.sun.content.api.common.utils;

import java.util.List;

/**
 * 按批次对list执行函数；
 *
 * @author sunshilong
 * @version 1.0
 * @date 2022/3/7
 */
public class ProcessByStep<V> {

    private int step = 50;
    private List<V> list;

    public ProcessByStep(List<V> list, int step) {
        this.list = list;
        this.step = step;
    }

    public ProcessByStep(List<V> list) {
        this.list = list;
    }

    public void execute(ListProcessable<V> listProcessable) {
        if (null == list) {
            return;
        }

        int length = list.size();

        for (int i = 0; i < length; i += step) {
            int end = i + step;
            listProcessable.process(list.subList(i, Math.min(end, length)));
        }
    }

}
