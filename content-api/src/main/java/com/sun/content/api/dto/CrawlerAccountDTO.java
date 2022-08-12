package com.sun.content.api.dto;

import lombok.Data;

import java.util.List;

/**
 * @author sunshilong
 * @version 1.0
 * @date 2022/5/10
 */
@Data
public class CrawlerAccountDTO extends CrawlerBaseDTO{

    private String stringTag;
    //账号集合
    private List<String> accountList;

}
