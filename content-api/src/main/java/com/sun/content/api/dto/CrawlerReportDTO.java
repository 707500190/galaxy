package com.sun.content.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.Map;

/**
 * @author sunshilong
 * @version 1.0
 * @date 2022/3/14
 */
@Data
public class CrawlerReportDTO extends CrawlerBaseDTO {


    @ApiModelProperty(value = "月统计开始时间")
    private Date monthStart;
    @ApiModelProperty(value = "月统计结束时间")
    private Date monthEnd;
    @ApiModelProperty(value = "年统计开始时间")
    private Date yearStart;
    @ApiModelProperty(value = "年统计结束时间")
    private Date yearEnd;

    Integer page = 1;
    Integer pageSize = 10;

    private Map<String, Boolean> sort;
}
