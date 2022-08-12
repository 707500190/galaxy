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
 * @描述：小红书主体
 * @作者: tw
 * @日期: 2022-02-23
 */

@Data
@ApiModel(value="小红书主体")
@TableName("red_content_zhiku")
public class RedContent implements EsSyncable{

    private static final long serialVersionUID=1L;

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键id")
    private Long id;
    @ApiModelProperty(value = "发布链接")
    private String publishLink;
    @ApiModelProperty(value = "标题")
    private String title;
    @ApiModelProperty(value = "发布时间")
    private Timestamp publishTime;
    @ApiModelProperty(value = "接口链接")
    private String interfaceLink;
    @ApiModelProperty(value = "文章ID")
    private String articleId;
    @ApiModelProperty(value = "文章点赞数")
    private Integer articleLikeNumber;
    @ApiModelProperty(value = "文章收藏数")
    private Integer articleCollectNumber;
    @ApiModelProperty(value = "文章评论数")
    private Integer articleCommentNumber;
    @ApiModelProperty(value = "文章转发数")
    private Integer articleForwardNumber;
    @ApiModelProperty(value = "文章内容")
    private String articleContent;
    @ApiModelProperty(value = "文章图片链接")
    private String articlePictureLink;
    @ApiModelProperty(value = "收录标签")
    private String collectionLabel;
    @ApiModelProperty(value = "是否收录")
    private String isInclude;
    @ApiModelProperty(value = "红人主页链接")
    private String homePageLink;
    @ApiModelProperty(value = "红人昵称")
    private String nickName;
    @ApiModelProperty(value = "小红书号")
    private String account;
    @ApiModelProperty(value = "红人粉丝数")
    private Integer fansNumber;
    @ApiModelProperty(value = "红人关注数")
    private Integer followNumber;
    @ApiModelProperty(value = "红人文章数")
    private Integer articleNumber;
    @ApiModelProperty(value = "红人性别")
    private String gender;
    @ApiModelProperty(value = "红人被收藏数")
    private Integer collectionNumber;
    @ApiModelProperty(value = "红人被点赞数")
    private Integer likeNumber;
    @ApiModelProperty(value = "红人简介")
    private String briefIntroduction;
    @ApiModelProperty(value = "红人头像链接")
    private String headPortraitLink;
    @ApiModelProperty(value = "红人等级")
    private String level;
    @ApiModelProperty(value = "红人等级图片链接")
    private String levelPictureLink;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "插入日期")
    private Timestamp insertDate;

    @TableField(exist = false)
    private String channel = "1";
}