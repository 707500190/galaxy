package com.sun.content.api.entity;

import java.util.Date;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import lombok.Data;

/**
 * @描述：
 * @作者: sunshilong
 * @日期: 2022-03-14
 */

@Data
@ApiModel(value="")
@TableName("crawler_strategy_account")
public class CrawlerStrategyAccount {

    private static final long serialVersionUID=1L;

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private Long id;
    @ApiModelProperty(value = "策略表id")
    private Long strategyId;
    @ApiModelProperty(value = "账号")
    private String account;
    @ApiModelProperty(value = "渠道")
    private String channel;
    @ApiModelProperty(value = "账号标签")
    private String accountTag;
    @ApiModelProperty(value = "异常状态")
    private String status;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
}