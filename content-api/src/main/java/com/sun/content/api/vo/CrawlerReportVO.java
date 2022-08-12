package com.sun.content.api.vo;

import lombok.Data;

import java.util.Date;


/**
 * @author sunshilong
 * @version 1.0
 * @date 2022/3/15
 */
@Data
public class CrawlerReportVO {

    private Long accountId;

    private String channel;

    private String account;

    private Date latestDate;

    private Integer countMonth;

    private Integer countYear;

    private String status;

    private Date createTime;
}
