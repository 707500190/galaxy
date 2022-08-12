package com.sun.content.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @描述：
 * @作者: sunshilong
 * @日期: 2022-05-23
 */

@Data
@ApiModel(value = "")
@TableName("DY_zhiku")
public class Tiktok implements EsSyncable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "")
    private Long id;
    @ApiModelProperty(value = "标题")
    private String title;
    @ApiModelProperty(value = "账号Id")
    private String accountId;
    @ApiModelProperty(value = "账号名称")
    private String account;
    @ApiModelProperty(value = "视频地址")
    private String videoUrl;
    @ApiModelProperty(value = "点赞数")
    private Integer likeNumber;
    @ApiModelProperty(value = "评论数")
    private Integer remarkNumber;
    @ApiModelProperty(value = "分享数")
    private Integer shareNumber;
    @ApiModelProperty(value = "插入的时间")
    private Timestamp insertDate;
    @ApiModelProperty(value = "冗余的JSON字段存储所有信息")
    private String allData;

    @TableField(exist = false)
    private String channel = "3";
}