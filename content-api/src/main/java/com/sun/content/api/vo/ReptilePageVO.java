package com.sun.content.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @author rebort
 * @description: 爬虫分页响应
 * @date 2022/2/24
 */
@Data
@ApiModel("爬虫分页响应")
public class ReptilePageVO {

    @ApiModelProperty(value = "渠道 1:小红书 2:微信公众号")
    private Integer channel;

    @ApiModelProperty(value = "先根据渠道区分之后再查对应的id")
    private Long id;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "账号")
    private String account;

    @ApiModelProperty(value = "标签")
    private String collectionLabel;

    @ApiModelProperty(value = "创建时间")
    private Timestamp insertDate;

    @ApiModelProperty(value = "发布时间")
    private Timestamp publishTime;

     @ApiModelProperty(value = "点赞数")
    private Integer articleLikeNumber;

}
