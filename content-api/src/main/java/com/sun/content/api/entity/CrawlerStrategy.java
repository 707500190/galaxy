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
 * @日期: 2022-03-14
 */

@Data
@ApiModel(value="")
@TableName("crawler_strategy")
public class CrawlerStrategy {

    private static final long serialVersionUID=1L;

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private Long id;
    @ApiModelProperty(value = "渠道")
    private String channel;
    @ApiModelProperty(value = "渠道名称")
    private String name;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

}