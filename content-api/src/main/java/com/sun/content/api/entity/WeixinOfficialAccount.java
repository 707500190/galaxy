package com.sun.content.api.entity;

import java.sql.Timestamp;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import lombok.Data;

/**
 * @描述：微信公众号
 * @作者: tw
 * @日期: 2022-02-23
 */

@Data
@ApiModel(value="微信公众号")
@TableName("weixin_official_account")
public class WeixinOfficialAccount implements EsSyncable{

    private static final long serialVersionUID=1L;

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键id")
    private Long id;
    @ApiModelProperty(value = "公众号")
    private String account;
    @ApiModelProperty(value = "标题")
    private String title;
    @ApiModelProperty(value = "微信公众号_biz")
    private String officialAccountBiz;
    @ApiModelProperty(value = "文章url")
    private String publishLink;
    @ApiModelProperty(value = "文章内容")
    private String articleContent;
    @ApiModelProperty(value = "文章html")
    private String articleHtml;
    @ApiModelProperty(value = "文章sn")
    private String articleSn;
    @ApiModelProperty(value = "发布时间")
    private Timestamp publishTime;
    @ApiModelProperty(value = "阅读数")
    private Integer readeNumber;
    @ApiModelProperty(value = "在看数")
    private Integer lookingAtNumber;
    @ApiModelProperty(value = "点赞数")
    private Integer articleLikeNumber;
    @ApiModelProperty(value = "插入日期")
    private Timestamp insertDate;
    /**
     * 目前忽略的字段，制作数据同步的时候用，后期考虑mysql字段
     */
    @TableField(exist = false)
    private String channel = "2";

}