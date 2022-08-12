package com.sun.content.api.vo;

import lombok.Data;

import java.util.List;

/**
 * @author sunshilong
 * @version 1.0
 * @date 2022/3/9
 */
@Data
public class  SearchDataResult <T> {

    private Integer total;

    private Integer pages;

    private List<T> content;

    private String scrollId;


}
