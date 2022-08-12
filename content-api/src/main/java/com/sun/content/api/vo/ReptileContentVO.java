package com.sun.content.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @author rebort
 * @description: 爬虫主体响应
 * @date 2022/2/24
 */
@Data
@ApiModel("爬虫主体响应")
public class ReptileContentVO {

    private Long id;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "渠道")
    private Integer channel;

    @ApiModelProperty(value = "小红书号")
    private String account;

    @ApiModelProperty(value = "发布时间")
    private Timestamp publishTime;

    @ApiModelProperty(value = "阅读数")
    private Integer readeNumber;

    @ApiModelProperty(value = "文章点赞数")
    private Integer articleLikeNumber;

    @ApiModelProperty(value = "文章内容")
    private String articleContent;

    @ApiModelProperty(value = "收录标签")
    private String collectionLabel;

    @ApiModelProperty(value = "发布链接")
    private String publishLink;

    @ApiModelProperty(value = "图片链接")
    private String articlePictureLink;

    @ApiModelProperty(value = "插入日期")
    private Timestamp insertDate;

}
