package com.sun.content.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @描述：
 * @作者: sunshilong
 * @日期: 2022-05-19
 */

@Data
@ApiModel(value = "")
@TableName("crawler_account_report")
public class CrawlerAccountReport {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "")
    private Long id;
    @ApiModelProperty(value = "")
    private Date latestDate;
    @ApiModelProperty(value = "")
    private Integer countMonth;
    @ApiModelProperty(value = "")
    private Integer countYear;
    @ApiModelProperty(value = "")
    private String account;
    @ApiModelProperty(value = "")
    private Date createTime;

    private String channel;
}