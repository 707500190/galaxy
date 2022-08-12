package com.sun.content.api.dto;

import lombok.Data;

/**
 * 用于接受同一类型区间的参数类
 *
 * @author sunshilong
 * @version 1.0
 * @date 2022/3/8
 */
@Data
public class Pair<T> {

    T start;

    T end;
}
