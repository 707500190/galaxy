package com.sun.content.api.entity;

import com.sun.content.api.common.constant.ElasticConstants;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.Date;

/**
 * ES文档类
 *
 * @author sunshilong
 * @version 1.0
 * @date 2022/3/7
 */
@Data
@Document(indexName = ElasticConstants.INDEX_NAME_DEFAULT_ALIAS)
public class ContentInfo implements Serializable{

    private static final long serialVersionUID = -1L;

    /**
     * ES id, channel + dbId;
     */
    @Id
    @Field(value = "id", type = FieldType.Keyword)
    private String id;

    @Field(value = "dbId", type = FieldType.Keyword)
    private Long dbId;

    @ApiModelProperty(value = "标题")
    @Field(value = "title", type = FieldType.Text)
    private String title;

    @ApiModelProperty(value = "渠道")
    @Field(value = "channel", type = FieldType.Keyword)
    private String channel;

    @ApiModelProperty(value = "小红书号")
    @Field(value = "account", type = FieldType.Text)
    private String account;

    @ApiModelProperty(value = "发布时间")
    @Field(value = "publishTime", type = FieldType.Date)
    private Date publishTime;

    @ApiModelProperty(value = "阅读数")
    @Field(value = "readeNumber", type = FieldType.Keyword)
    private Integer readeNumber;

    @ApiModelProperty(value = "文章点赞数")
    @Field(value = "articleLikeNumber", type = FieldType.Long)
    private Integer articleLikeNumber;

    @ApiModelProperty(value = "文章内容")
    @Field(value = "articleContent", type = FieldType.Text)
    private String articleContent;

    @ApiModelProperty(value = "收录标签")
    @Field(value = "collectionLabel", type = FieldType.Text)
    private String collectionLabel;

    @ApiModelProperty(value = "发布链接")
    @Field(value = "publishLink", type = FieldType.Text)
    private String publishLink;

    @ApiModelProperty(value = "图片链接")
    @Field(value = "articlePictureLink", type = FieldType.Text)
    private String articlePictureLink;

    @ApiModelProperty(value = "插入日期")
    @Field(value = "insertDate", type = FieldType.Date)
    private Date insertDate;

    @ApiModelProperty(value = "账号标签")
    @Field(value = "accountTag", type = FieldType.Text)
    private String accountTag;

    @ApiModelProperty(value = "抖音视频路径")
    @Field(value = "videoPath", type = FieldType.Text)
    private String videoPath;
}
