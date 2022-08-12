package com.sun.content.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author sunshilong
 * @version 1.0
 * @date 2022/3/10
 */
@Data
public class QueryContentDTO implements Serializable {


    private static final long serialVersionUID = -1L;

    @Id
    private String id;

    private Long dbId;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "渠道")
    private String channel;

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

    @ApiModelProperty(value = "微信公众号_biz")
    private String officialAccountBiz;

    @ApiModelProperty(value = "文章html")
    private String articleHtml;

    @ApiModelProperty(value = "文章sn")
    private String articleSn;

    @ApiModelProperty(value = "在看数")
    private Integer lookingAtNumber;

    @ApiModelProperty(value = "插入日期")
    private Timestamp insertDate;


    @ApiModelProperty(value = "抖音视频路径")
    private String videoPath;
}


